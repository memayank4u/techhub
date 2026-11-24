package com.techhub.app.features.crypto.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.app.core.common.Resource
import com.techhub.app.features.crypto.domain.usecase.GetCoinsUseCase
import com.techhub.app.features.crypto.domain.usecase.SearchCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val searchCoinsUseCase: SearchCoinsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CryptoUiState())
    val uiState: StateFlow<CryptoUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadCoins(forceRefresh = false)
    }

    fun onEvent(event: CryptoUiEvent) {
        when (event) {
            is CryptoUiEvent.Refresh -> loadCoins(forceRefresh = true)
            is CryptoUiEvent.OnSearchQueryChanged -> updateSearchQuery(event.query)
            is CryptoUiEvent.ClearSearch -> updateSearchQuery("")
            is CryptoUiEvent.OnCoinClicked -> {
                // Handled in navigation
            }
        }
    }

    private fun loadCoins(forceRefresh: Boolean) {
        viewModelScope.launch {
            getCoinsUseCase(forceRefresh = forceRefresh).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = current.coins.isEmpty(),
                                isRefreshing = forceRefresh,
                                coins = resource.data ?: current.coins,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isOffline = false,
                                coins = resource.data,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isOffline = true,
                                coins = resource.data ?: current.coins,
                                errorMessage = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update { it.copy(filteredCoins = emptyList()) }
            return
        }

        searchJob = searchCoinsUseCase(query)
            .onEach { filtered ->
                _uiState.update { it.copy(filteredCoins = filtered) }
            }
            .launchIn(viewModelScope)
    }
}
