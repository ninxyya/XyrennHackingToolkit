package com.xyrenn.hacking.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences("xyrenn_prefs", Context.MODE_PRIVATE)

    object Keys {
        const val FIRST_LAUNCH = "first_launch"
        const val THEME_MODE = "theme_mode"
        const val STEALTH_MODE = "stealth_mode"
        const val AUTO_START = "auto_start"
        const val SAVE_LOGS = "save_logs"
        const val NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val ATTACK_TIMEOUT = "attack_timeout"
        const val MAX_THREADS = "max_threads"
        const val DEFAULT_LHOST = "default_lhost"
        const val DEFAULT_LPORT = "default_lport"
        const val LAST_USER = "last_user"
        const val LAST_LOGIN = "last_login"
    }

    fun isFirstLaunch(): Boolean = prefs.getBoolean(Keys.FIRST_LAUNCH, true)

    fun setFirstLaunchComplete() {
        prefs.edit().putBoolean(Keys.FIRST_LAUNCH, false).apply()
    }

    fun getThemeMode(): String = prefs.getString(Keys.THEME_MODE, "dark") ?: "dark"

    fun setThemeMode(mode: String) {
        prefs.edit().putString(Keys.THEME_MODE, mode).apply()
    }

    fun isStealthMode(): Boolean = prefs.getBoolean(Keys.STEALTH_MODE, false)

    fun setStealthMode(enabled: Boolean) {
        prefs.edit().putBoolean(Keys.STEALTH_MODE, enabled).apply()
    }

    fun isAutoStart(): Boolean = prefs.getBoolean(Keys.AUTO_START, true)

    fun setAutoStart(enabled: Boolean) {
        prefs.edit().putBoolean(Keys.AUTO_START, enabled).apply()
    }

    fun isSaveLogs(): Boolean = prefs.getBoolean(Keys.SAVE_LOGS, true)

    fun setSaveLogs(enabled: Boolean) {
        prefs.edit().putBoolean(Keys.SAVE_LOGS, enabled).apply()
    }

    fun areNotificationsEnabled(): Boolean = prefs.getBoolean(Keys.NOTIFICATIONS_ENABLED, true)

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(Keys.NOTIFICATIONS_ENABLED, enabled).apply()
    }

    fun getAttackTimeout(): Int = prefs.getInt(Keys.ATTACK_TIMEOUT, 30)

    fun setAttackTimeout(timeout: Int) {
        prefs.edit().putInt(Keys.ATTACK_TIMEOUT, timeout).apply()
    }

    fun getMaxThreads(): Int = prefs.getInt(Keys.MAX_THREADS, 5)

    fun setMaxThreads(threads: Int) {
        prefs.edit().putInt(Keys.MAX_THREADS, threads).apply()
    }

    fun getDefaultLhost(): String = prefs.getString(Keys.DEFAULT_LHOST, "192.168.1.100") ?: "192.168.1.100"

    fun setDefaultLhost(lhost: String) {
        prefs.edit().putString(Keys.DEFAULT_LHOST, lhost).apply()
    }

    fun getDefaultLport(): Int = prefs.getInt(Keys.DEFAULT_LPORT, 4444)

    fun setDefaultLport(port: Int) {
        prefs.edit().putInt(Keys.DEFAULT_LPORT, port).apply()
    }

    fun getLastUser(): String = prefs.getString(Keys.LAST_USER, "") ?: ""

    fun setLastUser(uid: String) {
        prefs.edit().putString(Keys.LAST_USER, uid).apply()
    }

    fun getLastLogin(): Long = prefs.getLong(Keys.LAST_LOGIN, 0)

    fun setLastLogin(time: Long) {
        prefs.edit().putLong(Keys.LAST_LOGIN, time).apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}