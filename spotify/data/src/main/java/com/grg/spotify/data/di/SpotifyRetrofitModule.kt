package com.grg.spotify.data.di

import com.grg.spotify.core.utils.Constants.DEFAULT_TIMEOUT_SECONDS
import com.grg.spotify.core.utils.Constants.Spotify_BASE_URL
import com.grg.spotify.data.networking.SpotifyAppService
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpotifyRetrofitModule {

    @Provides
    @Singleton
    @Named("data")
    fun providesHttpClient(authenticator: Authenticator): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .authenticator(authenticator)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitSpotifyService(@Named("data") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Spotify_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAuthService(retrofit: Retrofit): SpotifyAppService =
        retrofit.create(SpotifyAppService::class.java)

}