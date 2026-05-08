package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.hustlemate.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var orders by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("orders")
            .whereEqualTo("userId", "demoUser")
            .addSnapshotListener { snapshot, _ ->
                loading = false
                if (snapshot != null) {
                    orders = snapshot.documents.map { 
                        val data = it.data ?: mutableMapOf()
                        data["id"] = it.id
                        data
                    }.sortedByDescending { it["createdAt"] as? Long ?: 0L }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders", color = White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SkyBlueDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
        ) {
            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SkyBlueDark)
                }
            } else if (orders.isEmpty()) {
                EmptyOrdersView()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(orders) { order ->
                        JumiaOrderCard(order)
                    }
                }
            }
        }
    }
}

@Composable
fun JumiaOrderCard(order: Map<String, Any>) {
    val status = (order["status"] as? String) ?: "PENDING"
    val timestamp = order["createdAt"] as? Long ?: 0L
    val dateStr = if (timestamp > 0) {
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(timestamp))
    } else "N/A"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Order ${order["id"].toString().take(8).uppercase()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Placed on $dateStr", fontSize = 12.sp, color = Color.Gray)
                }
                
                StatusBadge(status)
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: KSh ${order["total"] ?: 0}",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                TextButton(
                    onClick = { /* Detail navigation */ },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text("SEE DETAILS", color = SkyBlueDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val backgroundColor = when (status.uppercase()) {
        "PAID", "DELIVERED", "SUCCESS" -> Success.copy(alpha = 0.1f)
        "CANCELLED", "FAILED" -> Failed.copy(alpha = 0.1f)
        else -> SkyBlueDark.copy(alpha = 0.1f)
    }
    
    val textColor = when (status.uppercase()) {
        "PAID", "DELIVERED", "SUCCESS" -> Success
        "CANCELLED", "FAILED" -> Failed
        else -> SkyBlueDark
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = status.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmptyOrdersView() {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(40.dp),
                color = White
            ) {
                Icon(
                    Icons.Default.ShoppingBag, 
                    contentDescription = null, 
                    modifier = Modifier.padding(20.dp).size(40.dp),
                    tint = SkyBlueDark
                )
            }
            Spacer(Modifier.height(16.dp))
            Text("You have no orders", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                "Items you order will appear here", 
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyOrdersPreview() {
    HustleMateTheme {
        MyOrdersScreen(rememberNavController())
    }
}
