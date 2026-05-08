package com.example.hustlemate.ui.theme.Selling

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hustlemate.components.AppTextField
import com.example.hustlemate.navigation.Routes
import com.example.hustlemate.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun AddProductScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val auth = FirebaseAuth.getInstance()

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var loading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Add Product",
            style = MaterialTheme.typography.headlineMedium,
            color = SkyBlueDark,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Image Preview
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No image selected", color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = SkyBlueDark),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("CHOOSE IMAGE", color = White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(value = name, label = "Product Name") { name = it }
        AppTextField(value = price, label = "Price (KSh)") { price = it }
        AppTextField(value = description, label = "Description") { description = it }
        AppTextField(value = category, label = "Category") { category = it }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    Toast.makeText(context, "Please log in first", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (imageUri == null) {
                    Toast.makeText(context, "Image is required", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (name.isBlank() || price.isBlank()) {
                    Toast.makeText(context, "Name and Price are required", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                loading = true
                val fileName = "products/${UUID.randomUUID()}.jpg"
                val ref = storage.reference.child(fileName)

                ref.putFile(imageUri!!)
                    .continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let { throw it }
                        }
                        ref.downloadUrl
                    }
                    .addOnSuccessListener { url ->
                        val product = hashMapOf(
                            "name" to name,
                            "price" to (price.toDoubleOrNull() ?: 0.0),
                            "description" to description,
                            "category" to category,
                            "imageUrl" to url.toString(),
                            "sellerId" to currentUser.uid,
                            "createdAt" to System.currentTimeMillis()
                        )

                        db.collection("products")
                            .add(product)
                            .addOnSuccessListener {
                                loading = false
                                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            .addOnFailureListener { e ->
                                loading = false
                                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                    }
                    .addOnFailureListener { e ->
                        loading = false
                        Toast.makeText(context, "Upload Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SkyBlueDark),
            shape = RoundedCornerShape(4.dp),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text("SAVE PRODUCT", fontWeight = FontWeight.Bold, color = White)
            }
        }
    }
}
