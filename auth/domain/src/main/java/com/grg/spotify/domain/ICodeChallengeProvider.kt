package com.grg.spotify.domain

interface ICodeChallengeProvider {

    fun getCodeVerifier(): String
    fun getCodeChallenge(): String
}