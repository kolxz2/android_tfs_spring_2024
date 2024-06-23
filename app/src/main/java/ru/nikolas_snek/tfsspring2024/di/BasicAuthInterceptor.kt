package ru.nikolas_snek.tfsspring2024.di

import okhttp3.Credentials
import okhttp3.Interceptor
import javax.inject.Inject

class BasicAuthInterceptor @Inject constructor(username: String, password: String) : Interceptor {
    private val credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val authenticatedRequest = chain.request().newBuilder()
            .header("Authorization", credentials)
            .build()
        return chain.proceed(authenticatedRequest)
    }
}