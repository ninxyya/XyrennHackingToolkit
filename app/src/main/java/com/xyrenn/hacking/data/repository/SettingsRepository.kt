package com.xyrenn.hacking.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xyrenn.hacking.ui.settings.models.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val STEALTH_MODE = booleanPreferencesKey("stealth_mode")
        val AUTO_START = booleanPreferencesKey("auto_start")
        val SAVE_LOGS = booleanPreferencesKey("save_logs")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val ATTACK_TIMEOUT = intPreferencesKey("attack_timeout")
        val MAX_THREADS = intPreferencesKey("max_threads")
        val DEFAULT_LHOST = stringPreferencesKey("default_lhost")
        val DEFAULT_LPORT = intPreferencesKey("default_lport")
    }

    val themeMode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME_MODE] ?: "dark"
        }

    val stealthMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.STEALTH_MODE] ?: false
        }

    val autoStart: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.AUTO_START] ?: true
        }

    val saveLogs: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SAVE_LOGS] ?: true
        }

    suspend fun updateThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode
        }
    }

    suspend fun updateStealthMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.STEALTH_MODE] = enabled
        }
    }

    suspend fun updateAutoStart(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_START] = enabled
        }
    }

    suspend fun updateSaveLogs(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SAVE_LOGS] = enabled
        }
    }

    suspend fun updateAttackSettings(timeout: Int, maxThreads: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ATTACK_TIMEOUT] = timeout
            preferences[PreferencesKeys.MAX_THREADS] = maxThreads
        }
    }

    suspend fun updateDefaultPayload(lhost: String, lport: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEFAULT_LHOST] = lhost
            preferences[PreferencesKeys.DEFAULT_LPORT] = lport
        }
    }

    suspend fun getAllSettings(): Map<String, Any?> {
        val preferences = context.dataStore.data
        return mapOf(
            "themeMode" to preferences.map { it[PreferencesKeys.THEME_MODE] },
            "stealthMode" to preferences.map { it[PreferencesKeys.STEALTH_MODE] },
            "autoStart" to preferences.map { it[PreferencesKeys.AUTO_START] },
            "saveLogs" to preferences.map { it[PreferencesKeys.SAVE_LOGS] }
        )
    }

    suspend fun clearSettings() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}