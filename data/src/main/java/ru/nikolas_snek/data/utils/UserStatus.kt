package ru.nikolas_snek.data.utils

enum class UserStatus(val textStatus: String) {
    OFFLINE("offline"),
    ACTIVE("active"),
    IDLE("idle");
    companion object {
        fun fromString(status: String): UserStatus {
            return when (status) {
                "offline" -> OFFLINE
                "active" -> ACTIVE
                "idle" -> IDLE
                else -> OFFLINE
            }
        }
    }
}
