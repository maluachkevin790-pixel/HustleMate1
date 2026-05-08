package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hustlemate.data.models.Product
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.example.hustlemate.viewmodel.CartViewModel
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    val db = FirebaseFirestore.getInstance()
    val productId = navController.currentBackStackEntry?.arguments?.getString("productId") ?: ""

    var product by remember { mutableStateOf<Product?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("products").document(productId).get()
            .addOnSuccessListener { doc ->
                product = Product(
                    id = doc.id,
                    name = doc.getString("name") ?: "",
                    description = doc.getString("description") ?: "",
                    price = doc.getDouble("price") ?: 0.0,
                    imageUrl = doc.getString("imageUrl") ?: ""
                )
                loading = false
            }
            .addOnFailureListener { loading = false }
    }

    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = SkyBlueDark)
        }
        return
    }

    val currentProduct = product ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details", color = White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SkyBlueDark)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = White) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Wishlist logic */ },
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Default.FavoriteBorder, null, tint = SkyBlueDark)
                    }
                    Button(
                        onClick = {
                            cartViewModel.addToCart(currentProduct)
                            navController.navigate(Routes.CART)
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlueDark),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("ADD TO CART", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // 🖼 Image Section
            AsyncImage(
                model = currentProduct.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(320.dp).background(White),
                contentScale = ContentScale.Fit
            )

            // 🏷 Info Section
            Column(modifier = Modifier.background(White).padding(16.dp).fillMaxWidth()) {
                Text(currentProduct.name, fontSize = 18.sp, color = TextPrimary)
                Spacer(Modifier.height(8.dp))
                Text("KSh ${currentProduct.price}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                
                // Ratings Row
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    repeat(4) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp)) }
                    Icon(Icons.Default.Star, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    Text("(24 reviews)", fontSize = 12.sp, color = SkyBlueDark, modifier = Modifier.padding(start = 4.dp))
                }
            }

            Spacer(Modifier.height(8.dp))

            // 📦 Delivery Info
            DetailSectionCard(title = "DELIVERY & RETURNS") {
                Column(modifier = Modifier.padding(12.dp)) {
                    DeliveryInfoRow(Icons.Default.LocalShipping, "Standard Delivery", "Scheduled between 24 Oct and 26 Oct")
                    HorizontalDivider(Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                    DeliveryInfoRow(Icons.Default.VerifiedUser, "Return Policy", "Free return within 15 days for official stores")
                }
            }

            Spacer(Modifier.height(8.dp))

            // 📝 Description Section
            DetailSectionCard(title = "PRODUCT DETAILS") {
                Text(
                    currentProduct.description,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            }

            Spacer(Modifier.height(100.dp)) // Space for bottom bar
        }
    }
}

@Composable
fun DetailSectionCard(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(White)) {
        Text(
            title,
            modifier = Modifier.padding(16.dp, 12.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color.Gray
        )
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
        content()
    }
}

@Composable
fun DeliveryInfoRow(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = SkyBlueDark)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
