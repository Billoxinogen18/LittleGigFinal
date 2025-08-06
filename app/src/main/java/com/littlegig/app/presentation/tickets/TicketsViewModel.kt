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
}

data class TicketsUiState(
    val tickets: List<Ticket> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)