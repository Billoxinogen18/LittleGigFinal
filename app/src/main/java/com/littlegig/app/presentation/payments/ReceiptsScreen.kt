package com.littlegig.app.presentation.payments

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.littlegig.app.data.repository.PaymentRecord
import com.littlegig.app.data.repository.PaymentRepository
import com.littlegig.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun ReceiptsScreen(navController: NavController, viewModel: ReceiptsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Receipts", style = MaterialTheme.typography.titleLarge)
        uiState.payments.forEach { p ->
            Text(text = "${p.date} • ${p.eventTitle} • ${p.amount} ${p.status}", style = MaterialTheme.typography.bodyMedium)
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