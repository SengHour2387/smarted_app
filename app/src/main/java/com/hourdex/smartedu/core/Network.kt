// RetrofitModule.kt
package com.hourdex.smartedu.core

import com.hourdex.smartedu.features.auth.AuthService
import com.hourdex.smartedu.features.classes.ClassesService
import com.hourdex.smartedu.features.edYears.EdYearsService
import com.hourdex.smartedu.features.enroll.EnrollService
import com.hourdex.smartedu.features.students.StudentService
import com.hourdex.smartedu.features.subjects.SubjectsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor  // We'll create this
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.110:3000/api/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideSubjectsService(retrofit: Retrofit): SubjectsService {
        return retrofit.create(SubjectsService::class.java)
    }
    @Provides
    @Singleton
    fun provideEdYearsService(retrofit: Retrofit): EdYearsService {
        return retrofit.create(EdYearsService::class.java)
    }

    @Provides
    @Singleton
    fun provideClassesService(retrofit: Retrofit): ClassesService {
        return retrofit.create(ClassesService::class.java)
    }

    //students
    @Provides
    @Singleton
    fun provideStudentsService( retrofit: Retrofit ): StudentService {
        return  retrofit.create(StudentService::class.java)
    }

    @Provides
    @Singleton
    fun provideEnrollStudent( retrofit: Retrofit ): EnrollService {
        return  retrofit.create(EnrollService::class.java)
    }
}