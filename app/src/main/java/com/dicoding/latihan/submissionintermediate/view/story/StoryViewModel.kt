package com.dicoding.latihan.submissionintermediate.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.latihan.submissionintermediate.data.StoryRepository
import com.dicoding.latihan.submissionintermediate.model.UserModel
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.response.ListStoryItem
import kotlinx.coroutines.launch

class StoryViewModel(private val pref: UserPreference, storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> = storyRepository.getStory().cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}