package com.grg.spotify.data.di

import com.grg.spotify.auth.repository.AuthRepository
import com.grg.spotify.auth.utils.CodeChallengeProvider
import com.grg.spotify.data.repository.SpotifyDataRepository
import com.grg.spotify.domain.networking.ICodeChallengeProvider
import com.grg.spotify.domain.repository.IAuthRepository
import com.grg.spotify.domain.repository.ISpotifyDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepository: AuthRepository): IAuthRepository

    @Binds
    @Singleton
    abstract fun provideCodeChallengeProvider(provider: CodeChallengeProvider): ICodeChallengeProvider

    @Binds
    @Singleton
    abstract fun provideSpotifyDataRepository(spotifyDataRepository: SpotifyDataRepository): ISpotifyDataRepository
}