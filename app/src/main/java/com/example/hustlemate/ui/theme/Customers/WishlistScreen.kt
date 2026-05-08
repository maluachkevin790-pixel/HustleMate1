package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hustlemate.data.models.CartManager
import com.example.hustlemate.data.models.Product
import com.example.hustlemate.data.repository.WishlistRepository
import com.example.hustlemate.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(navController: NavController) {

    val userId = "demoUser"
    var items by remember { mutableStateOf(listOf<Product>()) }

    LaunchedEffect(Unit) {
        WishlistRepository.listen(userId) {
            items = it
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wishlist", color = White) },
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
                .padding(16.dp)
        ) {
            if (items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No saved items yet",
                        color = TextSecondary
                    )
                }
            } else {
                LazyColumn {
                    items(items) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    text = product.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = SkyBlueDark
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "KES ${product.price}",
                                    color = TextPrimary
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = {
                                            CartManager.add(product)
                                            WishlistRepository.remove(userId, product.id)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SkyBlueDark,
                                            contentColor = White
                                        )
                                    ) {
                                        Text("Move to Cart")
                                    }

                                    TextButton(
                                        onClick = {
                                            WishlistRepository.remove(userId, product.id)
                                        }
                                    ) {
                                        Text("Remove", color = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WishlistPreview() {
    HustleMateTheme {
        WishlistScreen(rememberNavController())
    }
}
