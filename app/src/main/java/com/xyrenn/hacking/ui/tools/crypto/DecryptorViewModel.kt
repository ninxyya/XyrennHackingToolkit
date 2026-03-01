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
class DecryptorViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoHelper: CryptoHelper
) : ViewModel() {

    private val _decryptedText = MutableLiveData("Decrypted text will appear here")
    val decryptedText: LiveData<String> = _decryptedText

    private val _isProcessing = MutableLiveData(false)
    val isProcessing: LiveData<Boolean> = _isProcessing

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    fun decryptText(encrypted: String, password: String) {
        viewModelScope.launch {
            _isProcessing.value = true
            _status.value = "Decrypting..."

            val decrypted = cryptoHelper.decryptAES(encrypted, password)
            if (decrypted != null) {
                _decryptedText.value = decrypted
                _status.value = "Decryption complete"
            } else {
                _status.value = "Decryption failed - wrong password or corrupted data"
            }
            _isProcessing.value = false
        }
    }

    fun pasteFromClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val pastedText = clip.getItemAt(0).text.toString()
            _decryptedText.value = pastedText
            _status.value = "Pasted from clipboard"
        } else {
            _status.value = "Clipboard is empty"
        }
    }

    fun clear() {
        _decryptedText.value = "Decrypted text will appear here"
        _status.value = "Cleared"
    }
}