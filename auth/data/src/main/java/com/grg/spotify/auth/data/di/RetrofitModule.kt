package com.grg.spotify.auth.data.di

import com.grg.spotify.auth.data.networking.AccessTokenAuthenticator
import com.grg.spotify.auth.data.networking.SpotifyAuthService
import com.grg.spotify.auth.data.utils.Constants.BASE_URL
import com.grg.spotify.auth.data.utils.Constants.DEFAULT_TIMEOUT_SECONDS
import com.grg.spotify.domain.networking.ICodeVerifierStore
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun providesAuthenticator(
        codeVerifierStore: ICodeVerifierStore,
        spotifyAuthService: dagger.Lazy<SpotifyAuthService>
    ): Authenticator =
        AccessTokenAuthenticator(codeVerifierStore, spotifyAuthService)

    @Provides
    @Singleton
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
    fun provideRetrofitService(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSpotifyAuthService(retrofit: Retrofit): SpotifyAuthService =
        retrofit.create(SpotifyAuthService::class.java)
}
