package io.sws.watchstack.core

import kotlinx.coroutines.CancellationException

inline fun <T> runSafely(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
