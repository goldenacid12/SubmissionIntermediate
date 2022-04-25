package com.dicoding.latihan.submissionintermediate.view.story

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
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
import com.dicoding.latihan.submissionintermediate.view.add.AddStoryActivity
import com.dicoding.latihan.submissionintermediate.view.login.LoginActivity
import com.dicoding.latihan.submissionintermediate.view.maps.MapsActivity
import com.dicoding.latihan.submissionintermediate.view.preferences.PreferencesActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    name = "settings"
)

class StoryActivity : AppCompatActivity() {

    private lateinit var storyViewModel: StoryViewModel
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        showLoading(false)
        setupView()
        setupViewModel()
        setupAction()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val settings = Intent(this, PreferencesActivity::class.java)
                startActivity(settings)
                true
            }
            else -> true
        }
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
        supportActionBar?.title = "  " + getString(R.string.story)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_logo_g)
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

    private fun setupAction() {
        storyViewModel.getUser().observe(this) { user ->
            val token = user.token
            Log.d(TOKEN, token)
            storyData(token)
        }
        binding.myButton.setOnClickListener {
            storyViewModel.logout()
            finish()
        }
        binding.addStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
        binding.maps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun storyData(token: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getStories("bearer $token")
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.d(TAG, "onSuccess: ${response.message()}")
                    showStory(responseBody.listStory as MutableList<ListStoryItem>)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showStory(log: MutableList<ListStoryItem>) {
        val listStoryAdapter = StoryAdapter(log, this)
        binding.recyclerView.adapter = listStoryAdapter

        listStoryAdapter.setOnItemClickListener(object : StoryAdapter.OnItemClickListener {
            override fun onItemClick(v: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
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

    companion object {
        private const val TAG = "StoryActivity"
        const val TOKEN = "TOKEN"
    }
}