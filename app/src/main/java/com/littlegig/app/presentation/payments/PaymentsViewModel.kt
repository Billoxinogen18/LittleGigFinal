package com.littlegig.app.presentation.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.littlegig.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState: StateFlow<PaymentsUiState> = _uiState.asStateFlow()

    init {
        loadPaymentData()
    }

    private fun loadPaymentData() {
        viewModelScope.launch {
            try {
                // 🔥 REAL PAYMENT DATA FROM FIRESTORE! 🔥
                val currentUser = authRepository.currentUser.first()
                if (currentUser != null) {
                    // Get real payment data from Firestore
                    val userPayments = firestore.collection("payments")
                        .whereEqualTo("userId", currentUser.id)
                        .get()
                        .await()
                    
                    val totalSpent = userPayments.documents.sumOf { 
                        it.getLong("amount")?.toInt() ?: 0 
                    }
                    
                    val currentMonth = java.time.LocalDate.now().monthValue
                    val monthlySpent = userPayments.documents
                        .filter { 
                            val paymentDate = it.getTimestamp("date")?.toDate()
                            paymentDate?.month == currentMonth - 1 // Java months are 0-based
                        }
                        .sumOf { 
                            it.getLong("amount")?.toInt() ?: 0 
                        }
                    
                    val recentTransactions = userPayments.documents
                        .sortedByDescending { it.getTimestamp("date") }
                        .take(10)
                        .map { doc ->
                            Transaction(
                                id = doc.id,
                                description = doc.getString("description") ?: "Payment",
                                amount = doc.getLong("amount")?.toInt() ?: 0,
                                type = doc.getString("type") ?: "payment",
                                status = doc.getString("status") ?: "completed",
                                date = doc.getTimestamp("date")?.toDate()?.toString() ?: "Unknown"
                            )
                        }
                    
                    _uiState.value = _uiState.value.copy(
                        totalSpent = totalSpent,
                        monthlySpent = monthlySpent,
                        recentTransactions = recentTransactions
                    )
                } else {
                    // No user logged in - show empty state
                    _uiState.value = _uiState.value.copy(
                        totalSpent = 0,
                        monthlySpent = 0,
                        recentTransactions = emptyList()
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load payment data: ${e.message}"
                )
            }
        }
    }

    fun setDefaultPaymentMethod(method: String) {
        _uiState.value = _uiState.value.copy(
            defaultPaymentMethod = method
        )
    }

    fun addPaymentMethod() {
        viewModelScope.launch {
            try {
                // 🔥 REAL PAYMENT METHOD ADDITION! 🔥
                val currentUser = authRepository.currentUser.first()
                if (currentUser != null) {
                    // Create a new payment method document in Firestore
                    val paymentMethod = hashMapOf(
                        "userId" to currentUser.id,
                        "type" to "mpesa", // Default to M-Pesa for Kenya
                        "name" to "M-Pesa",
                        "isDefault" to true,
                        "createdAt" to com.google.firebase.Timestamp.now()
                    )
                    
                    firestore.collection("payment_methods")
                        .add(paymentMethod)
                        .await()
                    
                    _uiState.value = _uiState.value.copy(
                        isSuccess = true,
                        successMessage = "Payment method added successfully!"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "User not authenticated"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to add payment method: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
} 