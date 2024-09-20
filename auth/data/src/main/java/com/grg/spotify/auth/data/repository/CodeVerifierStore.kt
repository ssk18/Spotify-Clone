package com.grg.spotify.auth.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.grg.core.domain.orElse
import com.grg.spotify.domain.networking.ICodeVerifierStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CodeVerifierStore(
    private val dataStore: DataStore<Preferences>,
) : ICodeVerifierStore {
    override fun getCodeVerifier(): String? {
        return getString(KEY_CODE_VERIFIER)
    }

    override fun saveCodeVerifier(codeVerifier: String?) {
        saveString(codeVerifier, KEY_CODE_VERIFIER)
    }

    override fun saveRequestState(state: String?) {
        saveString(state, KEY_REQUEST_STATE)
    }

    override fun getRequestState(): String? {
        return getString(KEY_REQUEST_STATE)
    }

    override fun getAccessToken(): String? {
        return getString(KEY_ACCESS_TOKEN)
    }

    override fun saveAccessToken(accessToken: String?) {
        saveString(accessToken, KEY_ACCESS_TOKEN)
    }

    override fun getRefreshToken(): String? {
        return getString(KEY_REFRESH_TOKEN)
    }

    override fun saveRefreshToken(refreshToken: String?) {
        saveString(refreshToken, KEY_REFRESH_TOKEN)
    }

    private fun saveString(data: String?, key: Preferences.Key<String>) {
        // TODO: Extremely bad and poor practice. change soon
        runBlocking {
            dataStore.edit { preferences ->
                data?.let {
                    preferences[key] = data
                }.orElse {

                    preferences.remove(key)
                }
            }
        }
    }

    private fun getString(key: Preferences.Key<String>): String? {
        // TODO: Extremely bad and poor practice. change soon
        return runBlocking {
            dataStore.data.first()[key]
        }
    }

    companion object {
        private val KEY_CODE_VERIFIER = stringPreferencesKey("code_verifier")
        private val KEY_REQUEST_STATE = stringPreferencesKey("request_state")
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}