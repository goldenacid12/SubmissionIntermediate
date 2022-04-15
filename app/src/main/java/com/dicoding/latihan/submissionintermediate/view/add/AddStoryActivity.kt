package com.dicoding.latihan.submissionintermediate.view.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.latihan.submissionintermediate.R
import com.dicoding.latihan.submissionintermediate.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.new_story)
    }
}