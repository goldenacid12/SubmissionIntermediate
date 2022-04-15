package com.dicoding.latihan.submissionintermediate.view.story

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.ViewModelFactory
import com.dicoding.latihan.submissionintermediate.api.ApiConfig
import com.dicoding.latihan.submissionintermediate.databinding.ActivityStoryBinding
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.response.ListStoryItem
import com.dicoding.latihan.submissionintermediate.response.StoriesResponse
import com.dicoding.latihan.submissionintermediate.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "settings")

class StoryActivity : AppCompatActivity() {

    private lateinit var storyViewModel: StoryViewModel
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        setupView()
        setupViewModel()
        setupAction()
        storyData()
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
        storyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[StoryViewModel::class.java]
        storyViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
    private fun setupAction(){
        binding.myButton.setOnClickListener{
            storyViewModel.logout()
        }
    }

    private fun storyData(){
        val bundle = intent.getStringExtra(TOKEN)
        val token: String? = bundle
        Log.d(TOKEN, token.toString())
        val client = token?.let { ApiConfig.getApiService().getStories("bearer $it") }
        client?.enqueue(object: Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ){
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    Log.d(TAG, "onSuccess: ${response.message()}")
                    showStory(responseBody.listStory as MutableList<ListStoryItem>)
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable){
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showStory(log: MutableList<ListStoryItem>){
        val listStoryAdapter = StoryAdapter(log, this)
        binding.recyclerView.adapter = listStoryAdapter
    }

    companion object{
        private const val TAG = "StoryActivity"
        const val TOKEN = "TOKEN"
    }
}