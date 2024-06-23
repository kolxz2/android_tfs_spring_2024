package ru.nikolas_snek.homework_1

internal sealed class ContactLogResponse<out IncomingCall> {
    data object Failure : ContactLogResponse<Nothing>()
    class Success<IncomingCall>(val data: IncomingCall) : ContactLogResponse<IncomingCall>()
}