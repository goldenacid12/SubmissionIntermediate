package com.dicoding.latihan.submissionintermediate.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.api.ApiConfig
import com.dicoding.latihan.submissionintermediate.databinding.ActivitySignUpBinding
import com.dicoding.latihan.submissionintermediate.response.PostSignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(binding.root)
        showLoading(false)
        playAnimation()
        setupView()
        setupAction()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
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

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.insert_name)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.insert_email)
                }
                !(Patterns.EMAIL_ADDRESS.matcher(email).matches()) -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_not_format)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.insert_password)
                }
                else -> {
                    signUpData(name, email, password)

                }
            }
        }
    }

    private fun signUpData(name: String, email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postSignUp(name, email, password)
        client.enqueue(object : Callback<PostSignUpResponse> {
            override fun onResponse(
                call: Call<PostSignUpResponse>,
                response: Response<PostSignUpResponse>

            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.e(TAG, "onSuccess: ${response.message()}")
                    if (responseBody.message != "Email is already taken") {
                        AlertDialog.Builder(this@SignUpActivity).apply {
                            setTitle(getString(R.string.sign_up))
                            setMessage(getString(R.string.account_create))
                            setPositiveButton("OK") { _, _ ->
                                supportFinishAfterTransition()
                            }
                            create()
                            show()
                        }
                    }
                } else {
                    if (responseBody?.message == "Email is already taken")
                    AlertDialog.Builder(this@SignUpActivity).apply {
                        setTitle(getString(R.string.sign_up))
                        setMessage(getString(R.string.email_used))
                        setPositiveButton("OK") { _, _ ->
                        }
                        create()
                        show()
                    }
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostSignUpResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val signUpTitle =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val name =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val email =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signUpButton =
            ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(logo, signUpTitle, name, email, password, signUpButton)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "SignUpActivity"
    }
}