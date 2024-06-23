package ru.nikolas_snek.chat.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.mockwebserver.MockWebServer
import ru.nikolas_snek.data.network.ZulipAPI
import ru.nikolas_snek.tfsspring2024.di.BasicAuthInterceptor

@Module
class TestRetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val USERNAME = "kolxz2001@gmail.com"
        val API_KEY = "6rPfeuOkJLFTyEssdcviTfDjuaqIkjkw"
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(BasicAuthInterceptor(USERNAME, API_KEY))
            .callTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, mockWebServer: MockWebServer): Retrofit {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideZulipAPI(retrofit: Retrofit): ZulipAPI {
        return retrofit.create(ZulipAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideMockWebServer(): MockWebServer {
        return MockWebServer()
    }
}
