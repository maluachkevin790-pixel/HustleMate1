package com.example.hustlemate.ui.theme.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hustlemate.components.AppTextField
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSeller by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val skyBlueGradient = Brush.verticalGradient(
        colors = listOf(SkyBlue, SkyBlueLight)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(skyBlueGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Create Account 🚀",
                        style = MaterialTheme.typography.headlineMedium,
                        color = SkyBlueDark,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    AppTextField(value = name, label = "Full Name") { name = it }
                    AppTextField(value = email, label = "Email") { email = it }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = null, tint = SkyBlueDark)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isSeller,
                            onCheckedChange = { isSeller = it },
                            colors = CheckboxDefaults.colors(checkedColor = SkyBlueDark)
                        )
                        Text("Register as Seller", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val trimmedEmail = email.trim()
                            val trimmedPass = password.trim()

                            if (name.isBlank() || trimmedEmail.isBlank() || trimmedPass.isBlank()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            auth.createUserWithEmailAndPassword(trimmedEmail, trimmedPass)
                                .addOnCompleteListener { authTask ->
                                    if (authTask.isSuccessful) {
                                        val userId = authTask.result?.user?.uid ?: ""
                                        val user = hashMapOf(
                                            "name" to name,
                                            "email" to trimmedEmail,
                                            "isSeller" to isSeller,
                                            "createdAt" to System.currentTimeMillis()
                                        )

                                        db.collection("users").document(userId).set(user)
                                            .addOnCompleteListener { dbTask ->
                                                isLoading = false
                                                if (dbTask.isSuccessful) {
                                                    Toast.makeText(context, "Welcome to HustleMate!", Toast.LENGTH_SHORT).show()
                                                    val route = if (isSeller) Routes.SELLER_DASHBOARD else Routes.HOME
                                                    navController.navigate(route) {
                                                        popUpTo(Routes.REGISTER) { inclusive = true }
                                                    }
                                                } else {
                                                    val error = dbTask.exception?.localizedMessage ?: "Unknown Database Error"
                                                    Toast.makeText(context, "Database Error: $error\nEnsure Firestore is ENABLED in console!", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                    } else {
                                        isLoading = false
                                        val error = authTask.exception?.localizedMessage ?: "Auth Error"
                                        val msg = if (error.contains("CONFIGURATION_NOT_FOUND")) {
                                            "Firebase Error: Enable 'Email/Password' in the Authentication tab of your Firebase Console!"
                                        } else error
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    }
                                }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlueDark),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text("REGISTER", fontWeight = FontWeight.Bold, color = White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = { navController.navigate(Routes.LOGIN) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Already have an account? Login", color = SkyBlueDark)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    HustleMateTheme {
        RegisterScreen(rememberNavController())
    }
}
