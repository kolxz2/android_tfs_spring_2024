package ru.nikolas_snek.channels.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SharedViewModel : ViewModel() {
    val allStreamsSearchQuery = MutableStateFlow<String?>(null)
    val subscribedStreamsSearchQuery = MutableStateFlow<String?>(null)
}
