package com.littlegig.app.presentation.payments

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.littlegig.app.data.repository.PaymentRecord
import com.littlegig.app.data.repository.PaymentRepository
import com.littlegig.app.data.repository.AuthRepository
import com.littlegig.app.presentation.components.AdvancedNeumorphicCard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

@Composable
fun ReceiptsScreen(navController: NavController, viewModel: ReceiptsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var filter by remember { mutableStateOf("all") }
    val payments = remember(uiState, filter) {
        when (filter) {
            "success" -> uiState.payments.filter { it.status.equals("success", true) }
            "pending" -> uiState.payments.filter { it.status.equals("pending", true) }
            "failed" -> uiState.payments.filter { it.status.equals("failed", true) }
            else -> uiState.payments
        }
    }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Receipts", style = MaterialTheme.typography.titleLarge)
        // Filter chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = filter=="all", onClick = { filter="all" }, label = { Text("All") })
            FilterChip(selected = filter=="success", onClick = { filter="success" }, label = { Text("Success") })
            FilterChip(selected = filter=="pending", onClick = { filter="pending" }, label = { Text("Pending") })
            FilterChip(selected = filter=="failed", onClick = { filter="failed" }, label = { Text("Failed") })
        }
        payments.forEach { p ->
            AdvancedNeumorphicCard {
                Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.weight(1f)) {
                        Text(text = p.eventTitle, style = MaterialTheme.typography.titleSmall)
                        Text(text = "${p.amount}", style = MaterialTheme.typography.bodyMedium)
                    }
                    val color = when (p.status.lowercase()) {
                        "success" -> Color(0xFF10B981)
                        "failed" -> Color(0xFFEF4444)
                        else -> Color(0xFFF59E0B)
                    }
                    AssistChip(onClick = {}, label = { Text(p.status.uppercase()) }, colors = AssistChipDefaults.assistChipColors(containerColor = color.copy(alpha = 0.15f), labelColor = color))
                }
            }
        }
    }
}

@HiltViewModel
class ReceiptsViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReceiptsUiState())
    val uiState: StateFlow<ReceiptsUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            val user = authRepository.currentUser.value ?: return@launch
            val res = paymentRepository.getPaymentHistory(user.id)
            res.onSuccess { list -> _uiState.value = _uiState.value.copy(payments = list) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(error = e.message) }
        }
    }
}

data class ReceiptsUiState(
    val payments: List<PaymentRecord> = emptyList(),
    val error: String? = null
)