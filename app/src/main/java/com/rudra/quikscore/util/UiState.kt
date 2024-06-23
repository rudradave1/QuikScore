package com.rudra.quikscore.util

sealed class UiState<T> {
    class Loading<T>: UiState<T>()
    data class Error<T>(val error: ErrorText) : UiState<T>()
    data class Loaded<T>(val data: T): UiState<T>()
}