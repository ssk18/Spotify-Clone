package com.grg.spotify.auth.data.di

import com.grg.spotify.auth.data.repository.AuthRepository
import com.grg.spotify.auth.data.utils.CodeChallengeProvider
import com.grg.spotify.domain.IAuthRepository
import com.grg.spotify.domain.ICodeChallengeProvider
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
}