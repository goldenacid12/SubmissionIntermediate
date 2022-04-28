package com.dicoding.latihan.submissionintermediate.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.ViewModelFactory
import com.dicoding.latihan.submissionintermediate.api.ApiConfig
import com.dicoding.latihan.submissionintermediate.databinding.ActivityLoginBinding
import com.dicoding.latihan.submissionintermediate.model.UserModel
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.response.PostLoginResponse
import com.dicoding.latihan.submissionintermediate.view.signup.SignUpActivity
import com.dicoding.latihan.submissionintermediate.view.story.StoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    name = "settings"
)

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(binding.root)

        playAnimation()
        setupView()
        setupViewModel()
        showLoading(false)
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
            ViewModelFactory(this, UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.insert_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.insert_password)
                }
                else -> {
                    loginData(email, password)
                    loginViewModel.login()
                }
            }
        }
        binding.signUpButton.setOnClickListener {
            Intent(this, SignUpActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun loginData(email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<PostLoginResponse> {
            override fun onResponse(
                call: Call<PostLoginResponse>,
                response: Response<PostLoginResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.e(TAG, "onSuccess: ${response.message()}")
                    Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    loginViewModel.token(
                        UserModel(
                            responseBody.loginResult.name,
                            email,
                            password,
                            false,
                            responseBody.loginResult.token
                        )
                    )
                    val intent = Intent(this@LoginActivity, StoryActivity::class.java)
                    startActivity(intent)
                    supportFinishAfterTransition()
                    finish()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@LoginActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@LoginActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val signInTitle =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val email =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val or = ObjectAnimator.ofFloat(binding.noAccount, View.ALPHA, 1f).setDuration(500)
        val signUp = ObjectAnimator.ofFloat(binding.signUpButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(logo, signInTitle, email, password, login, or, signUp)
            startDelay = 500
        }.start()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}