package com.dicoding.latihan.submissionintermediate.view.story

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.ViewModelFactory
import com.dicoding.latihan.submissionintermediate.databinding.ActivityStoryBinding
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.view.add.AddStoryActivity
import com.dicoding.latihan.submissionintermediate.view.login.LoginActivity
import com.dicoding.latihan.submissionintermediate.view.maps.MapsActivity
import com.dicoding.latihan.submissionintermediate.view.preferences.PreferencesActivity

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    name = "settings"
)

class StoryActivity : AppCompatActivity() {

    private val storyViewModel: StoryViewModel by viewModels { ViewModelFactory(this, UserPreference.getInstance(dataStore)) }
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(binding.root)

        showLoading(false)
        setupView()
        setupViewModel()
        setupAction()

        showStory()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
        storyViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun setupAction() {
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

    private fun showStory() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val storyAdapter = StoryAdapter()

        binding.recyclerView.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        storyViewModel.story.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }



        storyAdapter.setOnItemClickListener(object : StoryAdapter.OnItemClickListener {
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