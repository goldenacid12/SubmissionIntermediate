package com.dicoding.latihan.submissionintermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.latihan.submissionintermediate.api.ApiService
import com.dicoding.latihan.submissionintermediate.database.StoryDatabase
import com.dicoding.latihan.submissionintermediate.response.ListStoryItem

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<ListStoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}