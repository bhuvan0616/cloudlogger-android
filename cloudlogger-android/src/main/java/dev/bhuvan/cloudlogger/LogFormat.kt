package dev.bhuvan.cloudlogger

interface LogFormat {

    fun getFormattedLog(
        timestamp: Long,
        osVersion: String,
        sdkVersion: Int,
        userName: String,
        userId: String,
        appId: String,
        logLevel: String,
        tag: String,
        message: String
    ): String
}