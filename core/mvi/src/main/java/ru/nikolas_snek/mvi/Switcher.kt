package com.example.mvi


import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class Switcher {
	private var currentChannel: SendChannel<*>? = null
	private val lock = Mutex()


	fun <Event : Any> switchInternal(
		delayMillis: Long = 0,
		action: () -> Flow<Event>,
	): Flow<Event> {
		return callbackFlow {
			lock.withLock {
				currentChannel?.close()
				currentChannel = channel
			}

			delay(delayMillis)

			action.invoke().collect { send(it) }

			channel.close()
		}
	}

	suspend fun cancelInternal(
		delayMillis: Long = 0,
	) {
		delay(delayMillis)
		lock.withLock {
			currentChannel?.close()
			currentChannel = null
		}
	}
}