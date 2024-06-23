package ru.nikolas_snek.chat.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkChangeReceiver(private val context: Context, private val onNetworkChange: (Boolean) -> Unit) {

	private val connectivityManager =
		context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

	private val networkCallback = object : ConnectivityManager.NetworkCallback() {
		override fun onAvailable(network: Network) {
			onNetworkChange(true)
		}

		override fun onLost(network: Network) {
			onNetworkChange(false)
		}
	}

	fun register() {
		val networkRequest = NetworkRequest.Builder()
			.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
			.build()
		connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
	}

	fun unregister() {
		connectivityManager.unregisterNetworkCallback(networkCallback)
	}
}