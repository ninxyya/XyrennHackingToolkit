package com.xyrenn.hacking.utils.helpers

import com.xyrenn.hacking.ui.tools.advanced.models.Script
import com.xyrenn.hacking.utils.managers.RootManager
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScriptHelper @Inject constructor(
    private val rootManager: RootManager
) {

    fun runScript(script: Script, params: String): String {
        return when (script.language) {
            "python" -> runPythonScript(script.content, params)
            "bash" -> runBashScript(script.content, params)
            else -> "Unsupported script language: ${script.language}"
        }
    }

    private fun runPythonScript(content: String, params: String): String {
        return try {
            // Simulate Python execution
            """
            Running Python script with params: $params
            [INFO] Script executed successfully
            [OUTPUT] Python simulation result
            """.trimIndent()
        } catch (e: Exception) {
            "Python error: ${e.message}"
        }
    }

    private fun runBashScript(content: String, params: String): String {
        return try {
            // Simulate Bash execution
            """
            Running Bash script with params: $params
            [INFO] Script executed successfully
            [OUTPUT] Bash simulation result
            """.trimIndent()
        } catch (e: Exception) {
            "Bash error: ${e.message}"
        }
    }

    fun saveScriptToFile(script: Script): Boolean {
        return try {
            val file = File("/sdcard/scripts/${script.name}.${script.language}")
            file.parentFile?.mkdirs()
            file.writeText(script.content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadScriptFromFile(name: String): Script? {
        return try {
            val file = File("/sdcard/scripts/$name")
            if (file.exists()) {
                val content = file.readText()
                val language = when {
                    name.endsWith(".py") -> "python"
                    name.endsWith(".sh") -> "bash"
                    else -> "unknown"
                }
                Script(0, name, language, content)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}