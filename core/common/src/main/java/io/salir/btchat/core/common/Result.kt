package io.salir.btchat.core.common

sealed class Result<out T, out P: Progress> {

    object Empty : Result<Nothing, Nothing>()

    data class Loading<out P: Progress>(val progress: P) : Result<Nothing, P>() {
        companion object {
            val Unspecified = Loading(Progress.Unspecified)
        }
    }

    data class Success<out T>(val data: T) : Result<T, Nothing>()

    data class Error(val exception: Exception) : Result<Nothing, Nothing>()
}

typealias ListResult<T> = Result<List<T>, Progress.WithData<List<T>>>

typealias SimpleDataResult<T> = Result<T, Progress.Unspecified>