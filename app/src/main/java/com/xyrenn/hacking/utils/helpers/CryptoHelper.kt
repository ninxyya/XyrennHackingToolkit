package com.xyrenn.hacking.utils.helpers

import java.security.MessageDigest
import java.util.Base64
import java.util.zip.CRC32
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoHelper @Inject constructor() {

    private val salt = "XyReNnSaLt123!".toByteArray()
    private val iv = "XyReNnIv12345678".toByteArray()

    fun encryptAES(text: String, password: String): String {
        return try {
            val key = generateKey(password)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
            val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
            Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            "Encryption failed: ${e.message}"
        }
    }

    fun decryptAES(encryptedText: String, password: String): String? {
        return try {
            val key = generateKey(password)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
            val decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText))
            String(decrypted, Charsets.UTF_8)
        } catch (e: Exception) {
            null
        }
    }

    private fun generateKey(password: String): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 10000, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    fun md5(input: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val hash = digest.digest(input.toByteArray())
        return bytesToHex(hash)
    }

    fun sha1(input: String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val hash = digest.digest(input.toByteArray())
        return bytesToHex(hash)
    }

    fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray())
        return bytesToHex(hash)
    }

    fun sha512(input: String): String {
        val digest = MessageDigest.getInstance("SHA-512")
        val hash = digest.digest(input.toByteArray())
        return bytesToHex(hash)
    }

    fun crc32(input: String): String {
        val crc = CRC32()
        crc.update(input.toByteArray())
        return crc.value.toString(16).uppercase().padStart(8, '0')
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
            hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
        }
        return String(hexChars)
    }

    fun base64Encode(input: String): String {
        return Base64.getEncoder().encodeToString(input.toByteArray())
    }

    fun base64Decode(input: String): String {
        return try {
            String(Base64.getDecoder().decode(input))
        } catch (e: Exception) {
            "Invalid Base64"
        }
    }
}