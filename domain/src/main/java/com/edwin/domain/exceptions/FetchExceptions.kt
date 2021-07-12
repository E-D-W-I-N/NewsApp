package com.edwin.domain.exceptions

sealed class FetchExceptions : Exception() {
    object RateLimited : FetchExceptions()
}