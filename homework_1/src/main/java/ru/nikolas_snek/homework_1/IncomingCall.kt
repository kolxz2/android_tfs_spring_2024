package ru.nikolas_snek.homework_1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class IncomingCall(
    val number:String,
    val callDate : String,
    val duration: String
) : Parcelable
