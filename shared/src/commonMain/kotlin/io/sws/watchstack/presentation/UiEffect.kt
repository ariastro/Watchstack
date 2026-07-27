package io.sws.watchstack.presentation

sealed interface UiEffect {
    data class Snackbar(
        val message: String,
        val actionLabel: String? = null,
        val actionId: String? = null
    ) : UiEffect
}
