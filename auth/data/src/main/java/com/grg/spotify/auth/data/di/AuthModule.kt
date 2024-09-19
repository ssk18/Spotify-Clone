package com.grg.spotify.auth.data.di

import android.content.Context
import com.grg.spotify.auth.data.repository.CodeVerifierStore
import com.grg.spotify.domain.ICodeVerifierStore
import com.grg.spotify.extensions.authDataStore
import dagger.Binds
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
        CodeVerifierStore(context.authDataStore)
}