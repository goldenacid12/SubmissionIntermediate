package com.dicoding.latihan.submissionintermediate.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.latihan.submissionintermediate.*
import com.dicoding.latihan.submissionintermediate.databinding.ActivityLoginBinding
import com.dicoding.latihan.submissionintermediate.model.UserModel
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.view.main.MainActivity
import com.dicoding.latihan.submissionintermediate.view.signup.SignUpActivity
import java.util.prefs.Preferences

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun setupAction(){
        binding.loginButton.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.insert_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.insert_password)
                }
                email != user.email -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_false)
                }
                password != user.password -> {
                    binding.passwordEditTextLayout.error = getString(R.string.password_false)
                }
                else -> {
                    loginViewModel.login()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        binding.signUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}