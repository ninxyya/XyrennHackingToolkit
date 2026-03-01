package com.xyrenn.hacking.utils.managers

import android.content.Context
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ShellUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    init {
        // Enable shell debugging
        Shell.enableVerboseLogging = false
        Shell.setDefaultBuilder(Shell.Builder.create().setFlags(Shell.FLAG_REDIRECT_STDERR))
    }

    fun isRootAvailable(): Boolean {
        return Shell.isAppGrantedRoot()
    }

    fun requestRootAccess(): Boolean {
        return Shell.getShell().isRoot
    }

    fun executeCommandAsRoot(command: String): String? {
        return try {
            val result = Shell.cmd(command).exec()
            if (result.isSuccess) {
                result.out.joinToString("\n")
            } else {
                result.err.joinToString("\n")
            }
        } catch (e: Exception) {
            null
        }
    }

    fun executeCommandsAsRoot(commands: List<String>): List<String> {
        return try {
            val result = Shell.cmd(commands).exec()
            if (result.isSuccess) {
                result.out
            } else {
                result.err
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun hasBinary(binaryName: String): Boolean {
        val result = Shell.cmd("which $binaryName").exec()
        return result.isSuccess && result.out.isNotEmpty()
    }
}