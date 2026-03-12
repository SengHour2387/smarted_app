package com.hourdex.smartedu.core


import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Skip adding token for auth endpoints
        if (request.url.encodedPath.contains("auth")) {
            return chain.proceed(request)
        }

        val token = runBlocking { tokenManager.tokenFlow.first() }

        return if (token != null) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}