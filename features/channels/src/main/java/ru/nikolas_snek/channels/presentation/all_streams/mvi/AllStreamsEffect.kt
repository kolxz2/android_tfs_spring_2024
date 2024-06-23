package ru.nikolas_snek.channels.presentation.mvi

import com.example.mvi.MviEffect

sealed interface AllStreamsEffect : MviEffect{
	data object NetworkError : AllStreamsEffect
}