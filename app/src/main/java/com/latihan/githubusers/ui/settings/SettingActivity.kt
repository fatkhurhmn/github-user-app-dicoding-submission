package com.latihan.githubusers.ui.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.latihan.githubusers.R
import com.latihan.githubusers.databinding.ActivitySettingsBinding
import com.latihan.githubusers.helper.SettingViewModelFactory
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLanguage()
        setPreferences()
        backButton()
    }

    private fun setPreferences() {
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(this, { isDarkMode: Boolean ->
            with(binding) {
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                    tvDarkMode.text = getString(R.string.dark_mode_on)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                    tvDarkMode.text = getString(R.string.dark_mode_off)
                }
            }
        })

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setLanguage() {
        with(binding) {
            btnLanguageSetting.setOnClickListener(this@SettingActivity)
            tvLanguage.text = Locale.getDefault().displayLanguage.toString()
        }
    }

    private fun backButton() {
        binding.settingsTopAppbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_language_setting -> {
                val intentLanguage = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intentLanguage)
            }
        }
    }
}