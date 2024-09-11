package com.grg.spotify.domain

interface ICodeVerifierStore {

    fun getCodeVerifier(): String?

    fun saveCodeVerifier(codeVerifier: String)
}