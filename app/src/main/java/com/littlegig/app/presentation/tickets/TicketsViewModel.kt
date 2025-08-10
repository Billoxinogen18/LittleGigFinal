package com.littlegig.app.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.Ticket
import com.littlegig.app.data.model.User
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import com.littlegig.app.utils.PaymentEventBus
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TicketsUiState())
    val uiState: StateFlow<TicketsUiState> = _uiState.asStateFlow()
    
    init {
        loadUserTickets()
        viewModelScope.launch {
            PaymentEventBus.events.collect { evt ->
                if (evt.success) {
                    _uiState.value = _uiState.value.copy(
                        isSuccess = true,
                        successMessage = "Payment successful! Your ticket has been added to your wallet."
                    )
                    loadUserTickets()
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Payment verification failed"
                    )
                }
            }
        }
    }
    
    private fun loadUserTickets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    ticketRepository.getUserTickets(user.id).collect { tickets ->
                        _uiState.value = _uiState.value.copy(
                            tickets = tickets.sortedByDescending { it.purchaseDate },
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        tickets = emptyList(),
                        isLoading = false
                    )
                }
            }
        }
    }
    
    fun cancelTicket(ticketId: String) {
        viewModelScope.launch {
            ticketRepository.cancelTicket(ticketId)
                .onSuccess {
                    // Ticket will be updated automatically through the flow
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }
    
    fun refreshTickets() {
        loadUserTickets()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun showQrScanner() {
        _uiState.value = _uiState.value.copy(showQrScanner = true)
    }
    
    fun hideQrScanner() {
        _uiState.value = _uiState.value.copy(showQrScanner = false)
    }

    fun handleQrCodeScan(qrData: String) {
        viewModelScope.launch {
            try {
                // Parse QR code data - could be a ticket ID, event URL, or other format
                // For now, we'll just show a success message
                _uiState.value = _uiState.value.copy(
                    isSuccess = true,
                    successMessage = "QR Code scanned: $qrData"
                )
                
                // In a real app, you would:
                // 1. Parse the QR data to extract ticket/event information
                // 2. Validate the ticket/event
                // 3. Update the ticket status or navigate to event details
                // 4. Show appropriate UI feedback
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to process QR code: ${e.message}"
                )
            }
        }
    }
}

data class TicketsUiState(
    val tickets: List<Ticket> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val showQrScanner: Boolean = false
)