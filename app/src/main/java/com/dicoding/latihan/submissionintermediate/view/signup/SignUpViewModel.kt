package com.dicoding.latihan.submissionintermediate.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.latihan.submissionintermediate.model.UserModel
import com.dicoding.latihan.submissionintermediate.model.UserPreference
import kotlinx.coroutines.launch

class SignupViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}