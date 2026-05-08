package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.example.hustlemate.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    var selectedPayment by remember { mutableStateOf("M-Pesa") }
    val total = cartViewModel.getTotal()
    val deliveryFee = 250.0
    val grandTotal = total + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", color = White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SkyBlueDark)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            if (selectedPayment == "Cash on Delivery") {
                                navController.navigate(Routes.orderConfirmation("COD-${System.currentTimeMillis()}"))
                            } else {
                                navController.navigate(Routes.PAYMENT)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlueDark),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("CONFIRM ORDER", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. DELIVERY ADDRESS SECTION
            item {
                CheckoutSectionCard(title = "ADDRESS DETAILS", icon = Icons.Default.LocationOn) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("John Doe", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("123 Business Park, Mombasa Road", fontSize = 13.sp, color = Color.Gray)
                        Text("Nairobi, Kenya", fontSize = 13.sp, color = Color.Gray)
                        Text("+254 700 000 000", fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }

            // 2. PAYMENT METHOD SECTION
            item {
                CheckoutSectionCard(title = "PAYMENT METHOD", icon = Icons.Default.Payment) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        PaymentOptionRow("M-Pesa", selectedPayment == "M-Pesa") { selectedPayment = "M-Pesa" }
                        PaymentOptionRow("PayPal / Card", selectedPayment == "PayPal") { selectedPayment = "PayPal" }
                        PaymentOptionRow("Cash on Delivery", selectedPayment == "Cash on Delivery") { selectedPayment = "Cash on Delivery" }
                    }
                }
            }

            // 3. ORDER SUMMARY SECTION
            item {
                CheckoutSectionCard(title = "ORDER SUMMARY") {
                    Column(modifier = Modifier.padding(12.dp)) {
                        SummaryRow("Subtotal", "KSh $total")
                        SummaryRow("Delivery Fee", "KSh $deliveryFee")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("KSh $grandTotal", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SkyBlueDark)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun CheckoutSectionCard(
    title: String,
    icon: ImageVector? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(icon, null, modifier = Modifier.size(18.dp), tint = Color.Gray)
                    Spacer(Modifier.width(8.dp))
                }
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            }
            content()
        }
    }
}

@Composable
fun PaymentOptionRow(label: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = SkyBlueDark)
        )
        Text(label, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckoutScreenPreview() {
    val navController = rememberNavController()
    HustleMateTheme {
        CheckoutScreen(navController)
    }
}
