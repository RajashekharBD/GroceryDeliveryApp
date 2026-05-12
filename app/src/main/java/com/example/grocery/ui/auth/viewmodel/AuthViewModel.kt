package com.example.grocery.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    private val _mobileNumber = MutableLiveData<String>()
    val mobileNumber: LiveData<String> = _mobileNumber

    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun setMobileNumber(number: String) {
        _mobileNumber.value = number
    }

    fun setIsLoggedIn(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }
}