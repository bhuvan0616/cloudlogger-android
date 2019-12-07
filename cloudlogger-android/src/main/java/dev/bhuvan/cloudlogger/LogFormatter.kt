package dev.bhuvan.cloudlogger

import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

class LogFormatter(
    private val logConfig: LogConfig
) : LogFormat {
    private val dateFormat = SimpleDateFormat(logConfig.timeFormat, Locale.ENGLISH)
    private val calendar = Calendar.getInstance()

    internal fun formatLog(logLevel: Int, tag: String, message: String): String {
        return getFormattedLog(
            System.currentTimeMillis(),
            Build.VERSION.RELEASE,
            Build.VERSION.SDK_INT,
            logConfig.userName,
            logConfig.userId,
            logConfig.appId,
            LogLevel.getLogLevel(logLevel).name,
            tag,
            message
        )
    }

    internal fun formatLog(logLevel: LogLevel, tag: String, message: String): String {
        return getFormattedLog(
            System.currentTimeMillis(),
            Build.VERSION.RELEASE,
            Build.VERSION.SDK_INT,
            logConfig.userName,
            logConfig.userId,
            logConfig.appId,
            logLevel.name,
            tag,
            message
        )
    }

    override fun getFormattedLog(
        timestamp: Long,
        osVersion: String,
        sdkVersion: Int,
        userName: String,
        userId: String,
        appId: String,
        logLevel: String,
        tag: String,
        message: String
    ): String {
        calendar.timeInMillis = timestamp
        val time = dateFormat.format(calendar.time)
        return "$time $appId | $osVersion($sdkVersion) | $userName($userId) [$logLevel/$tag]: $message"
    }
}