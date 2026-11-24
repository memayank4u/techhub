package com.techhub.app.features.crypto.presentation

sealed interface CryptoUiEvent {
    data object Refresh : CryptoUiEvent
    data class OnSearchQueryChanged(val query: String) : CryptoUiEvent
    data object ClearSearch : CryptoUiEvent
    data class OnCoinClicked(val coinId: String) : CryptoUiEvent
}
