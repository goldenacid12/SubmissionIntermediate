package com.dicoding.latihan.submissionintermediate.view.preferences

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.ViewModelFactory
import com.dicoding.latihan.submissionintermediate.databinding.ActivityPreferencesBinding
import com.dicoding.latihan.submissionintermediate.model.UserModel
import com.dicoding.latihan.submissionintermediate.model.UserPreference


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferencesBinding
    private lateinit var preferencesViewModel: PreferencesViewModel

    lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this, UserPreference.getInstance(dataStore))
        )[PreferencesViewModel::class.java]

        preferencesViewModel.getUser().observe(this) { user ->
            val token = binding.token
            token.text = user.token
        }

        supportActionBar?.title = "  " + getString(R.string.preferences)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_logo_g)
    }
}