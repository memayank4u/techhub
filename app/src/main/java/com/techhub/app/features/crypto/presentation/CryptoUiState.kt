package com.techhub.app.features.crypto.presentation

import com.techhub.app.features.crypto.domain.model.Coin

data class CryptoUiState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val filteredCoins: List<Coin> = emptyList()
) {
    val displayList: List<Coin>
        get() = if (searchQuery.isBlank()) coins else filteredCoins
}
