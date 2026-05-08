package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.hustlemate.data.models.CartManager
import com.example.hustlemate.data.models.Product
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val db = remember { FirebaseFirestore.getInstance() }
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Product>()) }
    var loading by remember { mutableStateOf(false) }

    // Fetch products from Firestore that match the query
    LaunchedEffect(query) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.length >= 2) {
            loading = true
            db.collection("products")
                .get()
                .addOnSuccessListener { snapshot ->
                    loading = false
                    val filtered = snapshot.documents.mapNotNull { doc ->
                        val name = doc.getString("name") ?: ""
                        val category = doc.getString("category") ?: ""
                        if (name.contains(trimmedQuery, ignoreCase = true) || category.contains(trimmedQuery, ignoreCase = true)) {
                            Product(
                                id = doc.id,
                                name = name,
                                description = doc.getString("description") ?: "",
                                price = doc.getDouble("price") ?: 0.0,
                                imageUrl = doc.getString("imageUrl") ?: "",
                                category = category
                            )
                        } else null
                    }
                    searchResults = filtered
                }
                .addOnFailureListener {
                    loading = false
                }
        } else {
            searchResults = emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Products", color = White) },
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
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search (e.g. Phones, Electronics)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SkyBlueDark,
                    focusedLabelColor = SkyBlueDark,
                    cursorColor = SkyBlueDark,
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true
            )

            if (loading) {
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SkyBlueDark)
                }
            } else if (searchResults.isEmpty() && query.trim().length >= 2) {
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No products found for \"$query\"", color = TextSecondary)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(searchResults) { product ->
                        SearchProductCard(product, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchProductCard(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("product_details/${product.id}") },
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = product.name,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "KSh ${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Button(
                    onClick = { 
                        CartManager.add(product)
                        navController.navigate(Routes.PAYMENT) 
                    },
                    modifier = Modifier.fillMaxWidth().height(34.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Success)
                ) {
                    Text("PAY NOW", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = White)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    val navController = rememberNavController()
    HustleMateTheme {
        SearchScreen(navController)
    }
}
