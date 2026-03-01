package com.xyrenn.hacking.utils.helpers

import android.content.Context
import com.xyrenn.hacking.utils.managers.RootManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermuxHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val rootManager: RootManager
) {

    fun executeCommand(command: String): String {
        return when {
            command == "help" -> getHelpText()
            command.startsWith("pkg install") -> installPackage(command)
            command == "ls" -> listFiles()
            command == "pwd" -> "/data/user/0/com.xyrenn.hacking/files"
            command == "whoami" -> "u0_a${android.os.Process.myUid()}"
            command.startsWith("cd ") -> changeDirectory(command)
            else -> executeShellCommand(command)
        }
    }

    private fun getHelpText(): String {
        return """
            Available commands:
            help                 - Show this help
            pkg install <pkg>    - Install package
            ls                   - List files
            pwd                  - Print working directory
            whoami               - Show current user
            cd <dir>             - Change directory
            ping <host>          - Ping host
            nmap <target>        - Run nmap (if installed)
            hydra <options>      - Run hydra (if installed)
            sqlmap <target>      - Run sqlmap (if installed)
            msfconsole           - Start Metasploit (if installed)
        """.trimIndent()
    }

    private fun installPackage(command: String): String {
        val packageName = command.removePrefix("pkg install ").trim()
        return when (packageName) {
            "nmap" -> "Installing nmap...\nnmap installation simulated"
            "hydra" -> "Installing hydra...\nhydra installation simulated"
            "sqlmap" -> "Installing sqlmap...\nsqlmap installation simulated"
            "metasploit" -> "Installing metasploit...\nmetasploit installation simulated (requires root)"
            else -> "Package $packageName not found"
        }
    }

    private fun listFiles(): String {
        return """
            Documents
            Downloads
            Pictures
            Music
            Videos
            Android
            termux_home
        """.trimIndent()
    }

    private fun changeDirectory(command: String): String {
        val dir = command.removePrefix("cd ").trim()
        return if (dir == "..") {
            "Changed to parent directory"
        } else {
            "Changed to $dir"
        }
    }

    private fun executeShellCommand(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            process.waitFor()
            if (output.isEmpty()) {
                "Command executed successfully (no output)"
            } else {
                output.toString()
            }
        } catch (e: Exception) {
            if (rootManager.isRootAvailable()) {
                rootManager.executeCommandAsRoot(command) ?: "Failed to execute command: ${e.message}"
            } else {
                "Command not available: ${e.message}"
            }
        }
    }
}