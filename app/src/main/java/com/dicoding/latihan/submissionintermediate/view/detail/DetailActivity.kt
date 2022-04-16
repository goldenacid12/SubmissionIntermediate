package com.dicoding.latihan.submissionintermediate.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.latihan.submissionintermediate.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        detailData()
    }

    private fun detailData() {
        val photo = binding.detailPhoto
        val user = binding.detailTextUser
        val desc = binding.detailTextDescription

        Glide.with(this).load(intent.getStringExtra(EXTRA_PHOTO)).into(photo)
        user.text = intent.getStringExtra(EXTRA_NAME)
        desc.text = intent.getStringExtra(EXTRA_DESC)

    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_PHOTO = "extra_photo"
    }
}