package com.example.hustlemate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hustlemate.network.MpesaRepository
import com.example.hustlemate.ui.theme.Customers.MpesaStatus
import kotlinx.coroutines.launch

class MpesaViewModel(private val repository: MpesaRepository = MpesaRepository()) : ViewModel() {
    var status by mutableStateOf(MpesaStatus.WAITING)
        private set
    
    var receiptCode by mutableStateOf("")
        private set

    fun initiatePayment(phoneNumber: String, amount: Int) {
        viewModelScope.launch {
            status = MpesaStatus.WAITING
            val response = repository.initiateSTKPush(phoneNumber, amount)
            
            if (response != null && response.responseCode == "0") {
                // The prompt was sent successfully. 
                // In a real app, you would now poll your backend or wait for a callback 
                // to see if the user actually entered their PIN.
                // For this demo, we'll simulate a successful payment after a delay.
                kotlinx.coroutines.delay(10000) 
                status = MpesaStatus.SUCCESS
                receiptCode = response.checkoutRequestID // Or actual receipt if available
            } else {
                status = MpesaStatus.FAILED
            }
        }
    }
    
    fun resetStatus() {
        status = MpesaStatus.WAITING
    }
}
