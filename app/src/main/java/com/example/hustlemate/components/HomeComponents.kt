package com.example.hustlemate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hustlemate.ui.theme.*

@Composable
fun BannerSection() {

    Card(
        colors = CardDefaults.cardColors(containerColor = Accent),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🔥 Big Sale Today!", color = TextPrimary)
            Text("Up to 50% OFF selected items", color = TextPrimary)
        }
    }
}

@Composable
fun CategoryChip(name: String) {

    Card(
        colors = CardDefaults.cardColors(containerColor = CardColor),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = TextPrimary
        )
    }
}

@Composable
fun ProductCardUI(
    name: String,
    price: String,
    onClick: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(containerColor = CardColor),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {

        Column(modifier = Modifier.padding(12.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Background)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(name, color = TextPrimary)
            Text(price, color = Accent)
        }
    }
}
