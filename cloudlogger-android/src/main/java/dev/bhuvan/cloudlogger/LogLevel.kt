package dev.bhuvan.cloudlogger

enum class LogLevel(val id: Int) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    ASSERT(7);

    companion object {
        fun getLogLevel(level: Int): LogLevel {
            return values().find { it.id == level } ?: VERBOSE
        }
    }
}