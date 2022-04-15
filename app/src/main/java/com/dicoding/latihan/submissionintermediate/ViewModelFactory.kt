package com.dicoding.latihan.submissionintermediate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import com.dicoding.latihan.submissionintermediate.view.login.LoginViewModel
import com.dicoding.latihan.submissionintermediate.view.main.MainViewModel
import com.dicoding.latihan.submissionintermediate.view.preferences.PreferencesViewModel
import com.dicoding.latihan.submissionintermediate.view.signup.SignupViewModel
import com.dicoding.latihan.submissionintermediate.view.story.StoryViewModel


class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(PreferencesViewModel::class.java) -> {
                PreferencesViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}