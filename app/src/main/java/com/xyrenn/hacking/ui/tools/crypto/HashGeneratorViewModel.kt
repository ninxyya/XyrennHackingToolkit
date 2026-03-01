package com.xyrenn.hacking.ui.tools.crypto

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.utils.helpers.CryptoHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HashGeneratorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoHelper: CryptoHelper
) : ViewModel() {

    private val _generatedHash = MutableLiveData("Hash will appear here")
    val generatedHash: LiveData<String> = _generatedHash

    private val _isProcessing = MutableLiveData(false)
    val isProcessing: LiveData<Boolean> = _isProcessing

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private val _compareResult = MutableLiveData("")
    val compareResult: LiveData<String> = _compareResult

    fun generateHash(text: String, hashType: String) {
        viewModelScope.launch {
            _isProcessing.value = true
            _status.value = "Generating $hashType hash..."

            val hash = when (hashType) {
                "MD5" -> cryptoHelper.md5(text)
                "SHA-1" -> cryptoHelper.sha1(text)
                "SHA-256" -> cryptoHelper.sha256(text)
                "SHA-512" -> cryptoHelper.sha512(text)
                "CRC32" -> cryptoHelper.crc32(text)
                else -> "Unsupported hash type"
            }

            _generatedHash.value = hash
            _status.value = "$hashType hash generated"
            _isProcessing.value = false
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Hash", text)
        clipboard.setPrimaryClip(clip)
        _status.value = "Copied to clipboard"
    }

    fun compareHashes(hash1: String, hash2: String) {
        viewModelScope.launch {
            val areEqual = hash1.equals(hash2, ignoreCase = true)
            _compareResult.value = if (areEqual) {
                "✓ Hashes MATCH"
            } else {
                "✗ Hashes DO NOT MATCH"
            }
        }
    }
}