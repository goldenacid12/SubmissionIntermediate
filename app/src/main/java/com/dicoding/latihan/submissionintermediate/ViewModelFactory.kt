package com.dicoding.latihan.submissionintermediate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.latihan.submissionintermediate.di.Injection
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.view.add.AddStoryViewModel
import com.dicoding.latihan.submissionintermediate.view.login.LoginViewModel
import com.dicoding.latihan.submissionintermediate.view.main.MainViewModel
import com.dicoding.latihan.submissionintermediate.view.maps.MapsViewModel
import com.dicoding.latihan.submissionintermediate.view.preferences.PreferencesViewModel
import com.dicoding.latihan.submissionintermediate.view.story.StoryViewModel


class ViewModelFactory(private val context: Context, private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(pref, Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(PreferencesViewModel::class.java) -> {
                PreferencesViewModel(pref) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}