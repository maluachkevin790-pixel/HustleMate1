package com.example.hustlemate.ui.theme.Selling

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hustlemate.navigation.Routes

import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SellerDashboardScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    var orders by remember { mutableStateOf(listOf<Pair<String, Map<String, Any>>>()) }

    LaunchedEffect(Unit) {
        db.collection("orders")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    orders = snapshot.documents.map {
                        Pair(it.id, it.data ?: emptyMap())
                    }
                }
            }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Routes.ADD_PRODUCT)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {

            Text("Seller Dashboard", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(orders) { (id, order) ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)) {
                        Column(Modifier.padding(12.dp)) {

                            Text("Order ID: $id")
                            Text("Total: KES ${order["total"]}")
                            Text("Status: ${order["status"]}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Row {
                                Button(onClick = {
                                    db.collection("orders").document(id)
                                        .update("status", "SHIPPED")
                                }) {
                                    Text("Ship")
                                }

                                Spacer(Modifier.width(8.dp))

                                Button(onClick = {
                                    db.collection("orders").document(id)
                                        .update("status", "DELIVERED")
                                }) {
                                    Text("Deliver")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
