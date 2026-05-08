package com.example.hustlemate.ui.theme.profille

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SkyBlueDark)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
        ) {
            // 1. User Header
            item {
                UserHeaderSection()
            }

            // 2. My Account Section
            item {
                ProfileSection(title = "MY ACCOUNT") {
                    ProfileMenuItem(Icons.Default.History, "Orders") {
                        navController.navigate(Routes.MY_ORDERS)
                    }
                    ProfileMenuItem(Icons.Default.Favorite, "Saved Items") {
                        navController.navigate(Routes.WISHLIST)
                    }
                    ProfileMenuItem(Icons.Default.Person, "Details") {
                        navController.navigate(Routes.EDIT_PROFILE)
                    }
                }
            }

            // 3. Settings Section
            item {
                ProfileSection(title = "SETTINGS") {
                    ProfileMenuItem(Icons.Default.Settings, "Settings") {
                        navController.navigate(Routes.SETTINGS)
                    }
                    ProfileMenuItem(Icons.Default.Help, "Help Center") {
                        navController.navigate(Routes.HELP)
                    }
                    ProfileMenuItem(Icons.Default.LocationOn, "Service Areas") {
                        navController.navigate(Routes.SERVICE_AREAS)
                    }
                }
            }

            // 4. Logout
            item {
                Spacer(Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text("LOGOUT", color = Color.Red, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun UserHeaderSection() {
    Surface(
        color = White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SkyBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = SkyBlueDark, modifier = Modifier.size(32.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Welcome, Kevin Maluach", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Kevinmaluach790@email.com", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ProfileSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Surface(color = White, modifier = Modifier.fillMaxWidth()) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(22.dp), tint = Color.DarkGray)
                Spacer(Modifier.width(16.dp))
                Text(label, fontSize = 15.sp)
            }
            Icon(Icons.AutoMirrored.Filled.NavigateNext, null, tint = Color.LightGray)
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    HustleMateTheme {
        ProfileScreen(rememberNavController())
    }
}
