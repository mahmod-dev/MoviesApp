package com.example.moviesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.moviesapp.roomDB.MovieDao
import com.example.moviesapp.roomDB.UserDao

class ViewModelFactory(val movieDao: MovieDao?=null, private val userDao: UserDao?=null) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            AuthViewModel(userDao!!) as T
        }
        else if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            MovieViewModel(movieDao!!) as T
        }
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}