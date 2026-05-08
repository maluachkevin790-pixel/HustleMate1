package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hustlemate.components.AppButton
import com.example.hustlemate.data.models.CartManager
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PaymentScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val items = CartManager.getItems()
    val total = CartManager.getTotal()
    
    var phoneNumber by remember { mutableStateOf("") }

    PaymentContent(
        total = total,
        phoneNumber = phoneNumber,
        onPhoneNumberChange = { phoneNumber = it },
        onPay = {
            if (items.isEmpty() || phoneNumber.isEmpty()) return@PaymentContent

            val order = hashMapOf(
                "userId" to "demoUser",
                "items" to items.map {
                    mapOf("name" to it.name, "price" to it.price)
                },
                "total" to total,
                "status" to "PENDING",
                "createdAt" to System.currentTimeMillis(),
                "phoneNumber" to phoneNumber
            )

            db.collection("orders")
                .add(order)
                .addOnSuccessListener { doc ->
                    val orderId = doc.id
                    // Navigate to Mpesa Status Screen to trigger STK Push
                    navController.navigate(
                        Routes.mpesaPaymentStatus(
                            orderId = orderId,
                            phoneNumber = phoneNumber,
                            amount = total.toInt()
                        )
                    )
                }
        }
    )
}

@Composable
fun PaymentContent(
    total: Double,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onPay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Payment",
                style = MaterialTheme.typography.headlineMedium,
                color = SkyBlueDark
            )

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Total Amount",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "KES $total",
                        style = MaterialTheme.typography.headlineSmall,
                        color = SkyBlueDark
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "M-Pesa Phone Number",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 2547XXXXXXXX") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SkyBlueDark,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Text(
                text = "Enter number in format 254712345678",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        AppButton(
            text = "Pay with M-Pesa",
            onClick = onPay
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentPreview() {
    HustleMateTheme {
        PaymentContent(
            total = 12500.0,
            phoneNumber = "254700000000",
            onPhoneNumberChange = {},
            onPay = {}
        )
    }
}
