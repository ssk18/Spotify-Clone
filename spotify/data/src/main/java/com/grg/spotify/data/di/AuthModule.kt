package com.grg.spotify.data.di

import android.content.Context
import com.grg.spotify.auth.repository.CodeVerifierStore
import com.grg.spotify.core.extensions.dataStore
import com.grg.spotify.domain.networking.ICodeVerifierStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideCodeVerifierStore(@ApplicationContext context: Context): ICodeVerifierStore =
        CodeVerifierStore(context.dataStore)
}