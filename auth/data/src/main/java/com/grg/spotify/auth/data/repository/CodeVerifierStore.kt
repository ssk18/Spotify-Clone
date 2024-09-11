package com.grg.spotify.auth.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.grg.spotify.domain.ICodeVerifierStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CodeVerifierStore(
    private val dataStore: DataStore<Preferences>,
) : ICodeVerifierStore {
    override fun getCodeVerifier(): String? {
        // TODO: Extremely bad and poor practice. change soon
        return runBlocking {
            dataStore.data.first()[KEY_CODE_VERIFIER]
        }
    }

    override fun saveCodeVerifier(codeVerifier: String) {
        // TODO: Extremely bad and poor practice. change soon
        runBlocking {
            dataStore.edit { preferences ->
                preferences[KEY_CODE_VERIFIER] = codeVerifier
            }
        }
    }

    companion object {
        private val KEY_CODE_VERIFIER = stringPreferencesKey("code_verifier")
    }
}