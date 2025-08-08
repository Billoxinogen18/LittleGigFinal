package com.littlegig.app.presentation.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class PaymentsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState: StateFlow<PaymentsUiState> = _uiState.asStateFlow()

    init {
        loadPaymentData()
    }

    private fun loadPaymentData() {
        viewModelScope.launch {
            try {
                // Load payment data from repository (will be implemented)
                val totalSpent = 2500
                val monthlySpent = 800
                val recentTransactions = listOf(
                    Transaction(
                        id = "1",
                        description = "Event Ticket - Summer Festival",
                        amount = 500,
                        type = "ticket",
                        status = "completed",
                        date = "2024-08-07"
                    ),
                    Transaction(
                        id = "2",
                        description = "Event Ticket - Tech Conference",
                        amount = 300,
                        type = "ticket",
                        status = "completed",
                        date = "2024-08-05"
                    ),
                    Transaction(
                        id = "3",
                        description = "Event Creation Fee",
                        amount = 100,
                        type = "event",
                        status = "completed",
                        date = "2024-08-03"
                    )
                )

                _uiState.value = _uiState.value.copy(
                    totalSpent = totalSpent,
                    monthlySpent = monthlySpent,
                    recentTransactions = recentTransactions
                )
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
        // Will be implemented when payment repository is ready
        _uiState.value = _uiState.value.copy(
            error = "Payment method addition will be implemented soon"
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
} 