package com.grg.spotify.auth.data.utils

import com.grg.spotify.domain.ICodeChallengeProvider
import com.grg.spotify.domain.ICodeVerifierStore
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.Base64
import javax.inject.Inject

class CodeChallengeProvider @Inject constructor(private val codeStore: ICodeVerifierStore) :
    ICodeChallengeProvider {
    private var verifier = ""

    override fun getCodeVerifier(): String {
        return verifier.takeIf { it.isNotBlank() } ?: run {
            codeStore.getCodeVerifier().orEmpty().also {
                verifier = it
            }
        }
    }

    override fun getCodeChallenge(): String {
        verifier = generateCodeVerifier()
        return generateCodeChallenge(verifier)
    }

    @Throws(UnsupportedEncodingException::class)
    private fun generateCodeVerifier(): String {
        return codeStore.getCodeVerifier() ?: run {
            val secureRandom = SecureRandom()
            val codeVerifier = ByteArray(64)
            secureRandom.nextBytes(codeVerifier)
            Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier).also { code ->
                codeStore.saveCodeVerifier(code)
            }
        }
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    private fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(charset("US-ASCII"))
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest: ByteArray = messageDigest.digest()
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
}