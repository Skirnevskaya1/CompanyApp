package com.example.models

class JsonFromServer<T>(
    var Items: T,
    var errors: Any,
    var debug: Any,
    var debugData: Any,
    var status: Int = 0
) {
    @JvmName("getItems1")
    fun getItems(): T {
        return Items
    }
}