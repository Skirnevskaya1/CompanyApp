package com.example.models

data class ConfigToRequest(
    val controller: String,
    val method: String,
    val subscribe: String,
    val data: String
)
