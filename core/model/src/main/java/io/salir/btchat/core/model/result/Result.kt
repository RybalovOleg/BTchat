package io.salir.btchat.core.model.result

import kotlin.collections.map

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

typealias SimpleResult<T> = Result<T, Progress.Unspecified>

@JvmName("mapListResult")
fun <T, R> ListResult<T>.map(mapper: (T) -> R): ListResult<R> = when (this) {
    is Result.Success -> Result.Success(data.map(mapper))
    is Result.Loading -> Result.Loading(Progress.WithData(progress.data.map(mapper)))
    is Result.Error -> this
    is Result.Empty -> this
}

@JvmName("mapSimpleResult")
fun <T, R> SimpleResult<T>.map(mapper: (T) -> R): SimpleResult<R> = when (this) {
    is Result.Success -> Result.Success(mapper(data))
    is Result.Loading -> this
    is Result.Error -> this
    is Result.Empty -> this
}