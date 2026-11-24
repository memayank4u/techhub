package com.techhub.app.core.common

/**
 * A sealed interface representing UI/Data state with offline caching support.
 * Both [Loading] and [Error] can hold optional cached data, enabling offline-first architecture.
 */
sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Loading<out T>(val data: T? = null) : Resource<T>
    data class Error<out T>(
        val message: String,
        val data: T? = null,
        val cause: Throwable? = null
    ) : Resource<T>
}
