package com.dicoding.latihan.submissionintermediate.di

import android.content.Context
import com.dicoding.latihan.submissionintermediate.api.ApiConfig
import com.dicoding.latihan.submissionintermediate.data.StoryRepository
import com.dicoding.latihan.submissionintermediate.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}