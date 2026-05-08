package com.example.hustlemate.ui.theme.Customers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hustlemate.R
import com.example.hustlemate.data.models.CartManager
import com.example.hustlemate.data.models.Product
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore

data class CategoryItem(val name: String, val image: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val db = remember { FirebaseFirestore.getInstance() }
    var products by remember { mutableStateOf(listOf<Product>()) }
    var loading by remember { mutableStateOf(true) }

    val categories = listOf(
        CategoryItem("Phones", R.drawable.phones),
        CategoryItem("Fashion", R.drawable.fashion),
        CategoryItem("Shoes", R.drawable.shoes),
        CategoryItem("Electronics", R.drawable.electronics),
        CategoryItem("Beauty", R.drawable.beauty),
        CategoryItem("Home", R.drawable.beauty),
        CategoryItem("Computing", R.drawable.electronics),
        CategoryItem("Groceries", R.drawable.phones)
    )

    LaunchedEffect(Unit) {
        db.collection("products").addSnapshotListener { snapshot, _ ->
            loading = false
            if (snapshot != null) {
                products = snapshot.documents.mapNotNull { doc ->
                    try {
                        Product(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            imageUrl = doc.getString("imageUrl") ?: "",
                            category = doc.getString("category") ?: ""
                        )
                    } catch (e: Exception) { null }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(White)
                            .clickable { navController.navigate(Routes.SEARCH) }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Search on HustleMate", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SkyBlueDark)
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                BannerSlider()
            }

            item(span = { GridItemSpan(2) }) {
                CategoryGrid(categories, navController)
            }

            item(span = { GridItemSpan(2) }) {
                FlashSalesSection(products.take(6), navController)
            }

            item(span = { GridItemSpan(2) }) {
                Text(
                    "Recommended for you",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (loading) {
                item(span = { GridItemSpan(2) }) {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = SkyBlueDark)
                    }
                }
            } else {
                items(products) { product ->
                    JumiaProductCard(product, navController) {
                        navController.navigate("product_details/${product.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun BannerSlider() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp))
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (page % 2 == 0) PrimaryBlue else SkyBlueDark)
                    .padding(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text("FLASH SALES", color = White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text("UP TO 50% OFF", color = White, fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = White, contentColor = SkyBlueDark)) {
                        Text("SHOP NOW")
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGrid(categories: List<CategoryItem>, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val rows = categories.chunked(4)
            rows.forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    rowItems.forEach { cat ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(70.dp)
                                .clickable { 
                                    navController.navigate("category/${cat.name}")
                                }
                        ) {
                            Image(
                                painter = painterResource(id = cat.image),
                                contentDescription = null,
                                modifier = Modifier.size(45.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(cat.name, fontSize = 11.sp, maxLines = 1, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlashSalesSection(flashProducts: List<Product>, navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().background(White, RoundedCornerShape(8.dp))) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.Red, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FlashOn, null, tint = White)
                Text("Flash Sales", color = White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text("Ends in: 04:20:15", color = White, fontSize = 12.sp)
            }
            Text("SEE ALL >", color = White, fontSize = 12.sp, modifier = Modifier.clickable { })
        }

        LazyRow(contentPadding = PaddingValues(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(flashProducts) { product ->
                Column(modifier = Modifier.width(100.dp).clickable { navController.navigate("product_details/${product.id}") }) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(product.name, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("KSh ${product.price}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun JumiaProductCard(product: Product, navController: NavController, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(product.name, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, minLines = 2)
                Spacer(Modifier.height(4.dp))
                Text("KSh ${product.price}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Text("KSh ${product.price + 500}", fontSize = 11.sp, color = Color.Gray, textDecoration = TextDecoration.LineThrough)
                
                Spacer(Modifier.height(8.dp))
                
                Button(
                    onClick = { 
                        CartManager.add(product)
                        navController.navigate(Routes.PAYMENT) 
                    },
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SkyBlueDark)
                ) {
                    Text("PAY NOW", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
