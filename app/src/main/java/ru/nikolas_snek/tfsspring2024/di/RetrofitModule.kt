package ru.nikolas_snek.tfsspring2024.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nikolas_snek.data.network.ZulipAPI
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
    }

    @Provides
    @Singleton
    fun provideZulipAPI(retrofit: Retrofit): ZulipAPI {
        return retrofit.create(ZulipAPI::class.java)
    }

    companion object{
        const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
        const val USERNAME = "kolxz2001@gmail.com"
        const val API_KEY = "6rPfeuOkJLFTyEssdcviTfDjuaqIkjkw"
    }

}
