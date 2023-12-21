package com.example.gieok_moa

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import java.util.Locale


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val languagePreference: ListPreference? = findPreference("language")
            languagePreference?.setOnPreferenceChangeListener(this)
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {

            if (preference.key == "language") {
                // 언어 변경 시 처리할 로직을 작성합니다.
                changeLocale(newValue as String)
            }

            return true
        }

        private fun changeLocale(localeLang: String) {
            val locale: Locale? = when (localeLang) {
                "Korean" -> Locale("ko")
                "English" -> Locale("en")
                else -> null
            }
            val config: Configuration = resources.configuration

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale)
            } else {
                config.locale = locale
            }

            resources.updateConfiguration(config, resources.displayMetrics)
        }


    }
}