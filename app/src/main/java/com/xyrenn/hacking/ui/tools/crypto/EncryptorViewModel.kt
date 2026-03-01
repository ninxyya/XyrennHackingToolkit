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
class EncryptorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoHelper: CryptoHelper
) : ViewModel() {

    private val _encryptedText = MutableLiveData("Encrypted text will appear here")
    val encryptedText: LiveData<String> = _encryptedText

    private val _isProcessing = MutableLiveData(false)
    val isProcessing: LiveData<Boolean> = _isProcessing

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    fun encryptText(text: String, password: String) {
        viewModelScope.launch {
            _isProcessing.value = true
            _status.value = "Encrypting..."

            val encrypted = cryptoHelper.encryptAES(text, password)
            _encryptedText.value = encrypted
            _status.value = "Encryption complete"
            _isProcessing.value = false
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Encrypted text", text)
        clipboard.setPrimaryClip(clip)
        _status.value = "Copied to clipboard"
    }

    fun clear() {
        _encryptedText.value = "Encrypted text will appear here"
        _status.value = "Cleared"
    }
}