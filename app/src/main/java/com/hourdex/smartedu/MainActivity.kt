package com.hourdex.smartedu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hourdex.smartedu.features.auth.AuthViewModel
import com.hourdex.smartedu.features.auth.LoginResult
import com.hourdex.smartedu.features.auth.LoginScreen
import com.hourdex.smartedu.features.dashboard.AdminNav
import com.hourdex.smartedu.ui.theme.SmartEduTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

// Define all routes
@Serializable
sealed class Routes(val route: String) {
    @Serializable
    data object Login : Routes("login")

    @Serializable
    data object Register : Routes("register")

    @Serializable
    data object Dashboard : Routes("dashboard")

    @Serializable
    data object ForgotPassword : Routes("forgot_password")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartEduTheme {
                // Create ViewModel at the top level
                val authViewModel: AuthViewModel = hiltViewModel()
                val loginState by authViewModel.loginState.collectAsStateWithLifecycle()

                // Navigation controller
                val navController = rememberNavController()

                // Handle navigation based on auth state
                LaunchedEffect(loginState) {
                    when (loginState) {
                        LoginResult.Success -> {
                            navController.navigate(Routes.Dashboard) {
                                // Clear back stack so users can't go back to login
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        LoginResult.Idle -> {
                            // Only navigate to login if not already there
                            if (navController.currentDestination?.route != Routes.Login::class.qualifiedName) {
                                navController.navigate(Routes.Login) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                        is LoginResult.Error -> {
                        }
                        else -> {} // Loading or Error - stay where you are
                    }
                }

                // Set up navigation graph
                    NavHost(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                        navController = navController,
                        startDestination = when (loginState) {
                            LoginResult.Success -> Routes.Dashboard
                            else -> Routes.Login
                        }
                    ) {
                        composable<Routes.Login> {
                            LoginScreen(
                                authViewModel = authViewModel,
                                onNavigateToRegister = {
                                    navController.navigate(Routes.Register)
                                },
                            )
                        }
                        composable<Routes.Dashboard> {
                            AdminNav(
                                authViewModel = authViewModel,
                            )
                        }
                    }
                // Show loading overlay if needed
                if (loginState == LoginResult.Loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}