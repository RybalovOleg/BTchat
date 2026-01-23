package io.salir.btchat.core.common

sealed class Result<out T> {

    object Empty : Result<Nothing>()

    data class Loading(val progress: Progress = Progress.Unspecified) : Result<Nothing>()

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: Exception) : Result<Nothing>()
}