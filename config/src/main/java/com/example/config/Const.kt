package com.example.config

object Const {
    // тип запроса данных
    const val LOCAL_REQUEST_METHOD = "LOCAL_REQUEST_METHOD"
    const val SERVER_REMOTE_REQUEST_METHOD = "SERVER_REMOTE_REQUEST_METHOD"
    const val SERVER_LOCAL_REQUEST_METHOD = "SERVER_LOCAL_REQUEST_METHOD"
    const val TEST_REQUEST_METHOD = "TEST_REQUEST_METHOD"

    // тип сборки проекта
    const val VERSION_PRODUCTION = 1
    const val VERSION_QAS = 2
    const val VERSION_DEBUG = 2

    // тип заголовка приложения (верхний бар)
    const val APP_BAR_MAIN = 1
    const val APP_BAR_SECOND = 2
    const val APP_BAR_MODAL = 3
    const val APP_BAR_ONLY_BACK = 4
}