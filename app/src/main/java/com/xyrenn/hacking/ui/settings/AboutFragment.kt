package com.xyrenn.hacking.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xyrenn.hacking.BuildConfig
import com.xyrenn.hacking.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInfo()
        setupClickListeners()
    }

    private fun setupInfo() {
        binding.tvVersion.text = "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        binding.tvAppName.text = "Xyrenn Hacking Toolkit"
        binding.tvDescription.text = "The Dark Knight of Android - Ultimate Hacking Toolkit\n\n" +
                "This application is designed for educational purposes only. " +
                "It contains various security testing tools to help you understand " +
                "and learn about cybersecurity concepts."
    }

    private fun setupClickListeners() {
        binding.btnGithub.setOnClickListener {
            openUrl("https://github.com/xyrenn/xyrenn-hacking-toolkit")
        }

        binding.btnTelegram.setOnClickListener {
            openUrl("https://t.me/xyrenn_dev")
        }

        binding.btnWebsite.setOnClickListener {
            openUrl("https://xyrenn.com")
        }

        binding.btnLicense.setOnClickListener {
            showLicenseDialog()
        }

        binding.btnDisclaimer.setOnClickListener {
            showDisclaimerDialog()
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showLicenseDialog() {
        val license = """
            MIT License

            Copyright (c) 2026 Xyrenn Team

            Permission is hereby granted, free of charge, to any person obtaining a copy
            of this software and associated documentation files (the "Software"), to deal
            in the Software without restriction, including without limitation the rights
            to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
            copies of the Software, and to permit persons to whom the Software is
            furnished to do so, subject to the following conditions:

            The above copyright notice and this permission notice shall be included in all
            copies or substantial portions of the Software.

            THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
            IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
            FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
            AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
            LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
            OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
            SOFTWARE.
        """.trimIndent()

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("License")
            .setMessage(license)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showDisclaimerDialog() {
        val disclaimer = """
            ⚠️ DISCLAIMER ⚠️

            This application is for EDUCATIONAL PURPOSES ONLY!

            The developers are NOT responsible for any misuse of this application. 
            By using this application, you agree that you are solely responsible 
            for your actions.

            This application should ONLY be used on networks and devices that you 
            own or have explicit permission to test.

            Unauthorized use of these tools against networks or devices you do not 
            own may violate local, state, and federal laws. The developers assume 
            no liability for any misuse or damages caused by this application.
        """.trimIndent()

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("⚠️ DISCLAIMER")
            .setMessage(disclaimer)
            .setPositiveButton("I Understand") { _, _ ->
                // User acknowledged
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}