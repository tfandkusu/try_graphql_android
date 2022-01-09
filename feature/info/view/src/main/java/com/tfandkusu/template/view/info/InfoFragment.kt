package com.tfandkusu.template.view.info

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.tfandkusu.template.info.view.R
import com.tfandkusu.template.model.AppInfo

class InfoFragment : PreferenceFragmentCompat() {
    companion object {
        private const val KEY_OSS = "oss"

        private const val KEY_ABOUT = "about"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_info, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreference<Preference>(KEY_OSS)?.setOnPreferenceClickListener {
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.title_oss_license))
            val intent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
            startActivity(intent)
            true
        }
        findPreference<Preference>(KEY_ABOUT)?.setOnPreferenceClickListener {
            showAboutDialog()
            true
        }
    }

    private fun showAboutDialog() {
        val adb = AlertDialog.Builder(requireContext())
        adb.setTitle(R.string.title_about)
        val sb = StringBuilder()
        sb.append(getString(R.string.app_name))
        sb.append('\n')
        sb.append(getString(R.string.version))
        sb.append(' ')
        sb.append(AppInfo.versionName)
        sb.append("\n\n")
        sb.append(getString(R.string.copyright))
        sb.append(' ')
        sb.append(getString(R.string.author_name))
        adb.setMessage(sb.toString())
        adb.setPositiveButton(R.string.ok) { _, _ ->
        }
        adb.show()
    }
}
