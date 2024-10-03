package com.grg.spotify.data.di

import com.grg.spotify.auth.networking.SpotifyAuthService
import com.grg.core.utils.Constants.BASE_URL
import com.grg.core.utils.Constants.DEFAULT_TIMEOUT_SECONDS
import com.grg.core.utils.Constants.Spotify_BASE_URL
import com.grg.spotify.data.dto.SerializedItem
import com.grg.spotify.data.networking.AccessTokenAuthenticator
import com.grg.spotify.data.networking.AuthTokenInterceptor
import com.grg.spotify.data.networking.SpotifyAppService
import com.grg.spotify.domain.networking.ICodeVerifierStore
import com.grg.spotify.domain.repository.IAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
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
    @Named("spotify_auth")
    fun providesAuthHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    @Named("spotify_auth")
    fun providesAuthRetrofit(@Named("spotify_auth") okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()

        // Configure the Json instance for serialization
        val json = Json {
            ignoreUnknownKeys = true  // To ignore unknown keys in the response
            isLenient = true          // Allow lenient JSON parsing
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(contentType)
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAuthService(@Named("spotify_auth") retrofit: Retrofit): SpotifyAuthService =
        retrofit.create(SpotifyAuthService::class.java)


    @Provides
    @Singleton
    @Named("spotify_data")
    fun providesHttpClient(
        @Named("spotify_auth") okHttpClient: OkHttpClient,
        authenticator: AccessTokenAuthenticator,
        interceptor: AuthTokenInterceptor
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .authenticator(authenticator)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("spotify_data")
    fun provideRetrofitService(@Named("spotify_data") okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()

        // Configure the Json instance for serialization
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        return Retrofit.Builder()
            .baseUrl(Spotify_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(contentType)
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAppService(@Named("spotify_data") retrofit: Retrofit): SpotifyAppService =
        retrofit.create(SpotifyAppService::class.java)
}


