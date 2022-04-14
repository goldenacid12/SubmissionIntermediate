package com.dicoding.latihan.submissionintermediate.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.latihan.submissionintermediate.*
import com.dicoding.latihan.submissionintermediate.databinding.ActivityMainBinding
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.view.login.LoginActivity
import java.util.prefs.Preferences
import javax.net.ssl.SSLSessionBindingEvent

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.title = getString(R.string.story)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
    private fun setupAction(){
        binding.myButton.setOnClickListener{
            mainViewModel.logout()
        }
    }
}