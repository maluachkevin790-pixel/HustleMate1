package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hustlemate.data.models.CartManager
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URL

// ----------------------
// MAIN SCREEN (LOGIC)
// ----------------------
@Composable
fun OrderConfirmationScreen(orderId: String, navController: NavController) {

    var status by remember { mutableStateOf("PENDING") }

    LaunchedEffect(Unit) {

        while (true) {

            try {
                // Use withContext(Dispatchers.IO) to avoid NetworkOnMainThreadException
                val response = withContext(Dispatchers.IO) {
                    URL("http://10.0.2.2:3000/status").readText()
                }

                if (response.contains("SUCCESS")) {

                    status = "SUCCESS"

                    FirebaseFirestore.getInstance()
                        .collection("orders")
                        .document(orderId)
                        .update("status", "PAID")

                    CartManager.clear()
                    break
                }

                if (response.contains("FAILED")) {
                    status = "FAILED"
                    break
                }

            } catch (_: Exception) {}

            delay(3000)
        }
    }

    OrderConfirmationContent(
        status = status,
        onTrackOrder = {
            navController.navigate(Routes.MY_ORDERS)
        },
        onRetry = {
            navController.popBackStack()
        }
    )
}

// ----------------------
// UI CONTENT (PREVIEWABLE)
// ----------------------
@Composable
fun OrderConfirmationContent(
    status: String,
    onTrackOrder: () -> Unit,
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (status) {

            "PENDING" -> {
                CircularProgressIndicator(color = SkyBlueDark)

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Check your phone for M-Pesa",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }

            "SUCCESS" -> {
                Text(
                    text = "Payment Successful",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SkyBlueDark
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onTrackOrder,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkyBlueDark,
                        contentColor = White
                    )
                ) {
                    Text("Track Order")
                }
            }

            "FAILED" -> {
                Text(
                    text = "Payment Failed",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkyBlueDark,
                        contentColor = White
                    )
                ) {
                    Text("Try Again")
                }
            }
        }
    }
}

// ----------------------
// PREVIEWS
// ----------------------
@Preview(showBackground = true)
@Composable
fun OrderPendingPreview() {
    HustleMateTheme {
        OrderConfirmationContent(
            status = "PENDING",
            onTrackOrder = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderSuccessPreview() {
    HustleMateTheme {
        OrderConfirmationContent(
            status = "SUCCESS",
            onTrackOrder = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderFailedPreview() {
    HustleMateTheme {
        OrderConfirmationContent(
            status = "FAILED",
            onTrackOrder = {},
            onRetry = {}
        )
    }
}
