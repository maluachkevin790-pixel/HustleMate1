package com.example.hustlemate.ui.theme.profille

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.hustlemate.ui.theme.*

/* ---------------------------------------------------
   ROUTES
--------------------------------------------------- */

object Routes {
    const val SETTINGS = "settings"
    const val LOGIN = "login"

    const val ACCOUNT_INFO = "account_info"
    const val PRIVACY = "privacy"
    const val NOTIFICATIONS = "notifications"
    const val HELP = "help"
    const val TERMS = "terms"
    const val ABOUT = "about"
}

/* ---------------------------------------------------
   MAIN NAVIGATION
--------------------------------------------------- */

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SETTINGS
    ) {

        composable(Routes.SETTINGS) {
            SettingsScreen(navController)
        }

        composable(Routes.ACCOUNT_INFO) {
            AccountInfoScreen(navController)
        }

        composable(Routes.PRIVACY) {
            PrivacyScreen(navController)
        }

        composable(Routes.NOTIFICATIONS) {
            NotificationScreen(navController)
        }

        composable(Routes.HELP) {
            HelpScreen(navController)
        }

        composable(Routes.TERMS) {
            TermsScreen(navController)
        }

        composable(Routes.ABOUT) {
            AboutScreen(navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen()
        }
    }
}

/* ---------------------------------------------------
   SETTINGS SCREEN
--------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Settings")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SkyBlueDark,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            /* ACCOUNT SECTION */

            SectionHeader("Account")

            SettingItem("Account Information") {
                navController.navigate(Routes.ACCOUNT_INFO)
            }

            SettingItem("Privacy & Security") {
                navController.navigate(Routes.PRIVACY)
            }

            SettingItem("Notification Settings") {
                navController.navigate(Routes.NOTIFICATIONS)
            }

            /* SUPPORT SECTION */

            SectionHeader("Support & About")

            SettingItem("Help Center") {
                navController.navigate(Routes.HELP)
            }

            SettingItem("Terms of Service") {
                navController.navigate(Routes.TERMS)
            }

            SettingItem("About HustleMate") {
                navController.navigate(Routes.ABOUT)
            }

            Spacer(modifier = Modifier.height(40.dp))

            /* LOGOUT BUTTON */

            Button(

                onClick = {

                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Failed,
                    contentColor = White
                )

            ) {

                Text("Logout")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/* ---------------------------------------------------
   SECTION HEADER
--------------------------------------------------- */

@Composable
fun SectionHeader(title: String) {

    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = SkyBlueDark,
        modifier = Modifier.padding(
            start = 16.dp,
            top = 24.dp,
            bottom = 8.dp
        )
    )
}

/* ---------------------------------------------------
   SETTING ITEM
--------------------------------------------------- */

@Composable
fun SettingItem(
    title: String,
    onClick: () -> Unit
) {

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = White
    ) {

        Column {

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = SkyBlueLight
            )
        }
    }
}

/* ---------------------------------------------------
   ACCOUNT INFO SCREEN
--------------------------------------------------- */

@Composable
fun AccountInfoScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Account Information",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Name: Cyril")
        Text("Email: cyril@gmail.com")
        Text("Phone: 0712345678")
    }
}

/* ---------------------------------------------------
   PRIVACY SCREEN
--------------------------------------------------- */

@Composable
fun PrivacyScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Privacy & Security",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("• Change Password")
        Spacer(modifier = Modifier.height(10.dp))

        Text("• Two-factor Authentication")
        Spacer(modifier = Modifier.height(10.dp))

        Text("• App Permissions")
    }
}

/* ---------------------------------------------------
   NOTIFICATION SCREEN
--------------------------------------------------- */

@Composable
fun NotificationScreen(navController: NavController) {

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Notification Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Enable Notifications",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsEnabled = it
                }
            )
        }
    }
}

/* ---------------------------------------------------
   HELP SCREEN
--------------------------------------------------- */

@Composable
fun HelpScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Help Center",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Email: support@hustlemate.com")
        Spacer(modifier = Modifier.height(10.dp))

        Text("Phone: +254 700 000 000")
    }
}

/* ---------------------------------------------------
   TERMS SCREEN
--------------------------------------------------- */

@Composable
fun TermsScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Terms of Service",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text =
                "By using HustleMate, you agree to our terms and conditions."
        )
    }
}

/* ---------------------------------------------------
   ABOUT SCREEN
--------------------------------------------------- */

@Composable
fun AboutScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "About HustleMate",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Version: 1.0.0")
        Spacer(modifier = Modifier.height(10.dp))

        Text("Built using Jetpack Compose")
    }
}

/* ---------------------------------------------------
   LOGIN SCREEN
--------------------------------------------------- */

@Composable
fun LoginScreen() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Login Screen",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

/* ---------------------------------------------------
   PREVIEW
--------------------------------------------------- */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsPreview() {

    HustleMateTheme {

        AppNavigation()
    }
}

/* ---------------------------------------------------
   SAMPLE COLORS
   REMOVE IF YOU ALREADY HAVE THEM
--------------------------------------------------- */

val SkyBlueDark = Color(0xFF1565C0)
val SkyBlueLight = Color(0xFF90CAF9)
val Background = Color(0xFFF5F5F5)
val White = Color.White
val TextPrimary = Color.Black
val TextSecondary = Color.Gray
val Failed = Color.Red