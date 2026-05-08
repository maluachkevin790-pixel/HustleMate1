package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hustlemate.components.AppButton
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.HustleMateTheme
import com.example.hustlemate.ui.theme.Success
import com.example.hustlemate.ui.theme.Failed
import com.example.hustlemate.ui.theme.TextPrimary
import com.example.hustlemate.ui.theme.Background
import com.example.hustlemate.viewmodel.MpesaViewModel

enum class MpesaStatus { WAITING, SUCCESS, FAILED }

@Composable
fun MpesaPaymentStatusScreen(
    navController: NavController,
    orderId: String,
    phoneNumber: String,
    amount: Int,
    viewModel: MpesaViewModel = viewModel()
) {
    val status = viewModel.status
    val receiptCode = viewModel.receiptCode

    // Trigger the STK Push prompt via the ViewModel when the screen is shown
    LaunchedEffect(orderId) {
        viewModel.initiatePayment(phoneNumber, amount)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (status) {
            MpesaStatus.WAITING -> {
                CircularProgressIndicator(color = Success)
                Spacer(Modifier.height(16.dp))
                Text("Confirming payment for order #$orderId...", color = TextPrimary)
                Spacer(Modifier.height(8.dp))
                Text("Check your phone for the M-Pesa prompt", color = TextPrimary)
                Spacer(Modifier.height(16.dp))
                AppButton("Resend Prompt") {
                    viewModel.initiatePayment(phoneNumber, amount)
                }
            }
            MpesaStatus.SUCCESS -> {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Success,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text("Payment Successful!", color = Success, style = MaterialTheme.typography.headlineSmall)
                Text("Receipt: $receiptCode", color = TextPrimary)
                Spacer(Modifier.height(16.dp))
                AppButton("Track my order") {
                    navController.navigate(Routes.MY_ORDERS) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            }
            MpesaStatus.FAILED -> {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = Failed,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text("Payment Failed", color = Failed, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                AppButton("Try Again") {
                    viewModel.initiatePayment(phoneNumber, amount)
                }
                Spacer(Modifier.height(8.dp))
                AppButton("Go Back") { navController.popBackStack() }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MpesaPaymentStatusScreenPreview() {
    val navController = rememberNavController()
    HustleMateTheme {
        MpesaPaymentStatusScreen(
            navController = navController,
            orderId = "ORD-12345",
            phoneNumber = "254700000000",
            amount = 1
        )
    }
}
