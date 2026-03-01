package com.xyrenn.hacking.ui.tools.spam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyrenn.hacking.ui.tools.spam.models.SpamTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpamTemplateViewModel @Inject constructor() : ViewModel() {

    private val _templates = MutableLiveData<List<SpamTemplate>>()
    val templates: LiveData<List<SpamTemplate>> = _templates

    private val templatesList = mutableListOf<SpamTemplate>()

    init {
        loadDefaultTemplates()
    }

    private fun loadDefaultTemplates() {
        templatesList.addAll(
            listOf(
                SpamTemplate(1, "Promo", "Dapatkan diskon 50% hari ini! Klik link berikut..."),
                SpamTemplate(2, "Undian", "Selamat! Anda memenangkan undian berhadiah..."),
                SpamTemplate(3, "Pengingat", "Jangan lupa bayar tagihan bulan ini..."),
                SpamTemplate(4, "Promo 2", "Beli 1 gratis 1 hanya hari ini..."),
                SpamTemplate(5, "Informasi", "Update sistem akan dilakukan malam ini...")
            )
        )
        _templates.value = templatesList
    }

    fun saveTemplate(name: String, content: String) {
        viewModelScope.launch {
            val newId = (templatesList.maxOfOrNull { it.id } ?: 0) + 1
            val template = SpamTemplate(newId, name, content)
            templatesList.add(0, template)
            _templates.value = templatesList.toList()
        }
    }

    fun deleteTemplate(id: Int) {
        viewModelScope.launch {
            templatesList.removeAll { it.id == id }
            _templates.value = templatesList.toList()
        }
    }
}