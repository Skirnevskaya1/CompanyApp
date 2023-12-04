package com.example.config

import com.example.config.Const.LOCAL_REQUEST_METHOD
import com.example.config.Const.VERSION_DEBUG

object Bootstrap {
    var REMOTE_SERVER_IP = "46.181.246.234"
    var REMOTE_SERVER_PORT = "7777"

    var LOCAL_SERVER_IP = "192.168.1.5"
    var LOCAL_SERVER_PORT = "80"

    var TYPE_BUILD = VERSION_DEBUG

    var DEFAULT_REQUEST_METHOD = LOCAL_REQUEST_METHOD
    var DEFAULT_URL_ADDRESS = REMOTE_SERVER_IP + ":" + REMOTE_SERVER_PORT
}