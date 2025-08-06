package com.littlegig.app.presentation.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.littlegig.app.data.model.Event
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.data.repository.EventRepository
import com.littlegig.app.data.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessDashboardViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BusinessDashboardUiState())
    val uiState: StateFlow<BusinessDashboardUiState> = _uiState.asStateFlow()
    
    private val _analytics = MutableStateFlow<BusinessAnalytics?>(null)
    val analytics: StateFlow<BusinessAnalytics?> = _analytics.asStateFlow()
    
    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val currentUser = authRepository.currentUser.first()
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "User not authenticated"
                    )
                    return@launch
                }
                
                // Load user's events
                val allEvents = eventRepository.getAllEvents().first()
                val userEvents = allEvents.filter { it.organizerId == currentUser.id }
                
                // Calculate analytics
                val totalRevenue = calculateTotalRevenue(userEvents)
                val totalCommission = totalRevenue * 0.04 // 4% commission
                val activeEvents = userEvents.count { it.isActive }
                val totalTicketsSold = userEvents.sumOf { it.ticketsSold }
                
                _analytics.value = BusinessAnalytics(
                    totalRevenue = totalRevenue,
                    totalCommission = totalCommission,
                    activeEvents = activeEvents,
                    totalTicketsSold = totalTicketsSold,
                    totalEvents = userEvents.size
                )
                
                _uiState.value = _uiState.value.copy(
                    events = userEvents.sortedByDescending { it.createdAt },
                    isLoading = false
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load dashboard data"
                )
            }
        }
    }
    
    private fun calculateTotalRevenue(events: List<Event>): Double {
        return events.sumOf { event ->
            event.ticketsSold * event.price
        }
    }
    
    fun refreshData() {
        loadDashboardData()
    }
}

data class BusinessDashboardUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class BusinessAnalytics(
    val totalRevenue: Double,
    val totalCommission: Double,
    val activeEvents: Int,
    val totalTicketsSold: Int,
    val totalEvents: Int
)