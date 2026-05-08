package com.example.hustlemate.ui.theme.profille

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hustlemate.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpCenterScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help Center", color = White) },
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
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SkyBlueDark)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "How can we help you?",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Contact Us", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SkyBlueDark)
                Spacer(Modifier.height(16.dp))

                HelpContactItem(Icons.Default.Phone, "Call Support", "+254 700 000 000")
                HelpContactItem(Icons.Default.Email, "Email Us", "support@hustlemate.com")
                HelpContactItem(Icons.Default.QuestionAnswer, "Live Chat", "Available 24/7")

                Spacer(Modifier.height(32.dp))

                Text("Frequently Asked Questions", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SkyBlueDark)
                Spacer(Modifier.height(16.dp))

                FaqItem("How to track my order?", "You can track your order in the 'My Orders' section of your profile.")
                FaqItem("What are the delivery charges?", "Delivery charges depend on your location and the size of the item.")
                FaqItem("How do I become a seller?", "You can register as a seller during the signup process or contact support.")
            }
        }
    }
}

@Composable
fun HelpContactItem(icon: ImageVector, title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = SkyBlueDark, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, color = androidx.compose.ui.graphics.Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(question, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Text(answer, color = TextSecondary, fontSize = 14.sp)
        HorizontalDivider(modifier = Modifier.padding(top = 12.dp), thickness = 0.5.dp, color = SkyBlueLight)
    }
}

@Preview(showBackground = true)
@Composable
fun HelpCenterPreview() {
    HustleMateTheme {
        HelpCenterScreen(rememberNavController())
    }
}
