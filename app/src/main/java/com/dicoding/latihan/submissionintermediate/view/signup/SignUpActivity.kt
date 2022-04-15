package com.dicoding.latihan.submissionintermediate.view.signup

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.ViewModelFactory
import com.dicoding.latihan.submissionintermediate.api.ApiConfig
import com.dicoding.latihan.submissionintermediate.databinding.ActivitySignUpBinding
import com.dicoding.latihan.submissionintermediate.model.UserModel
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.response.PostSignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "settings")

class SignUpActivity : AppCompatActivity() {
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
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
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]
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
                password.length < 6 ->{
                    binding.passwordEditTextLayout.error = getString(R.string.password_min)
                }
                else -> {
                    signUpData(name, email, password)

                }
            }
        }
    }

    private fun signUpData(name: String, email: String, password: String){
        val client = ApiConfig.getApiService().postSignUp(name, email, password)
        client.enqueue(object: Callback<PostSignUpResponse>{
            override fun onResponse(
                call: Call<PostSignUpResponse>,
                response: Response<PostSignUpResponse>
            ){
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    Log.e(TAG, "onSuccess: ${response.message()}")
                    if (responseBody.message != "Email is already taken"){
                        signupViewModel.saveUser(UserModel(name, email, password, false,""))
                        AlertDialog.Builder(this@SignUpActivity).apply {
                            setTitle(getString(R.string.sign_up))
                            setMessage(getString(R.string.account_create))
                            setPositiveButton("OK") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }else{
                    if(responseBody?.message == "Email is already taken")
                    signupViewModel.saveUser(UserModel(name, email, password, false,""))
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
            override fun onFailure(call: Call<PostSignUpResponse>, t: Throwable){
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    companion object{
        private const val TAG = "SignUpActivity"
    }
}