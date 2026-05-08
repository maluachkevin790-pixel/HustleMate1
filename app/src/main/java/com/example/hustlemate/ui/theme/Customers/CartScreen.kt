package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hustlemate.models.Cart
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.example.hustlemate.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart (${cartViewModel.cartItems.size})", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SkyBlueDark)
            )
        },
        bottomBar = {
            if (cartViewModel.cartItems.isNotEmpty()) {
                CartSummaryBar(total = cartViewModel.getTotal()) {
                    navController.navigate(Routes.CHECKOUT)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
        ) {
            if (cartViewModel.cartItems.isEmpty()) {
                EmptyCartView()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartViewModel.cartItems) { item ->
                        JumiaCartItem(
                            item = item,
                            onIncrease = { cartViewModel.increaseQty(item.product.id) },
                            onDecrease = { cartViewModel.decreaseQty(item.product.id) },
                            onRemove = { cartViewModel.removeFromCart(item.product.id) }
                        )
                    }
                    
                    item {
                        Spacer(Modifier.height(100.dp)) // Space for bottom bar
                    }
                }
            }
        }
    }
}

@Composable
fun JumiaCartItem(
    item: Cart,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, maxLines = 2, fontSize = 14.sp)
                Text("KSh ${item.product.price}", fontWeight = FontWeight.Bold, color = SkyBlueDark)
                
                Spacer(Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onRemove, contentPadding = PaddingValues(0.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = SkyBlueDark, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("REMOVE", color = SkyBlueDark, fontSize = 12.sp)
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Remove, null, tint = SkyBlueDark)
                        }
                        Text("${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp))
                        IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Add, null, tint = SkyBlueDark)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartSummaryBar(total: Double, onCheckout: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", fontWeight = FontWeight.Bold)
                Text("KSh $total", fontWeight = FontWeight.Bold, color = SkyBlueDark, fontSize = 18.sp)
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SkyBlueDark),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("CHECKOUT (KSh $total)")
            }
        }
    }
}

@Composable
fun EmptyCartView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Your cart is empty!", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            Text("Browse our categories and discover our best deals!")
        }
    }
}
