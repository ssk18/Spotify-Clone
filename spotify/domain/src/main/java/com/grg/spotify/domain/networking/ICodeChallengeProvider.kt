package com.grg.spotify.domain.networking

interface ICodeChallengeProvider {

    fun getCodeVerifier(): String

    fun getCodeChallenge(): String
}