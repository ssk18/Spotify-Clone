package com.grg.spotify.auth.data.di

import android.content.Context
import com.grg.spotify.auth.data.repository.AuthRepository
import com.grg.spotify.auth.data.repository.CodeVerifierStore
import com.grg.spotify.auth.data.utils.CodeChallengeProvider
import com.grg.spotify.domain.IAuthRepository
import com.grg.spotify.domain.ICodeChallengeProvider
import com.grg.spotify.domain.ICodeVerifierStore
import com.grg.spotify.extensions.authDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {

    @Provides
    @ActivityScoped
    fun provideAuthRepository(authRepository: AuthRepository): IAuthRepository = authRepository

    @Provides
    @Singleton
    fun provideCodeChallengeProvider(provider: CodeChallengeProvider): ICodeChallengeProvider =
        provider

    @Provides
    @Singleton
    fun provideCodeVerifierStore(@ApplicationContext context: Context): ICodeVerifierStore =
        CodeVerifierStore(context.authDataStore)

}