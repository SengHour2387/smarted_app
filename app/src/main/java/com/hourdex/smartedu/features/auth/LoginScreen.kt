package com.hourdex.smartedu.features.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hourdex.smartedu.uis.components.LLTextField

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToRegister: ()-> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    val loginState by authViewModel.loginState.collectAsStateWithLifecycle()
    val errorMessage = (loginState as? LoginResult.Error)?.message

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        LLTextField(
            text = email,
            onTextChange = { email = it },
            placeHolderText = "Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        LLTextField(
            text = password,
            onTextChange = { password = it },
            placeHolderText = "Password",
            isPassword = true,
            keyboardType = KeyboardType.Password,
        )

        Spacer(modifier = Modifier.height(8.dp))


        Box(

        ) {
            TextButton(
                onClick = {
                    expanded.value = !expanded.value
                }
            ) { Text("Select Role")}
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Admin") },
                    onClick = { role = "admin"
                    expanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Student") },
                    onClick = { role = "student"
                        expanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Teacher") },
                    onClick = { role = "teacher"
                        expanded.value = false
                    }
                )
            }
        }


        Text("Role: $role", fontWeight = FontWeight.SemiBold,fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.login(email,role,password)
            },
            enabled = email.isNotBlank() && password.isNotBlank()
        ) {
            Text("Login")
        }
    }
}