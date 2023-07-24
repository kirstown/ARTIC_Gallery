package dev.kirstenbaker.gallery.model

sealed class RemoteResult<T> {
    class Success<T>(val data: T) : RemoteResult<T>()
    class Failure<T>(val errorMessage: String) : RemoteResult<T>()
}