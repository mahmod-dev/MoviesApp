package com.example.moviesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.entity.Movie
import com.example.moviesapp.roomDB.MovieDao
import com.example.moviesapp.util.Resource
import kotlinx.coroutines.launch

class MovieViewModel(
    private val movieDao: MovieDao
) : ViewModel() {
    val insertMovieLiveData = MutableLiveData<Resource<Long>>()
    val editMovieLiveData = MutableLiveData<Resource<Long>>()
    val rateMovieLiveData = MutableLiveData<Resource<Long>>()
    val allMoviesLiveData = MutableLiveData<Resource<List<Movie>>>()
    val searchMoviesLiveData = MutableLiveData<Resource<List<Movie>>>()
    val deleteLiveData = MutableLiveData<Resource<Long>>()


    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            insertMovieLiveData.postValue(Resource.loading(null))
            try {
                movieDao.insert(movie)
                insertMovieLiveData.postValue(Resource.success(1))
            } catch (e: Exception) {
                // handler error
                insertMovieLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun deleteMovie(id: Int) {
        viewModelScope.launch {
            deleteLiveData.postValue(Resource.loading(null))
            try {
                movieDao.deleteMovie(id)
                deleteLiveData.postValue(Resource.success(1))
            } catch (e: Exception) {
                // handler error
                deleteLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun getAllMovies() {
        viewModelScope.launch {
            allMoviesLiveData.postValue(Resource.loading(null))
            try {
                val userFromDb = movieDao.getAllMovies()
                allMoviesLiveData.postValue(Resource.success(userFromDb))
            } catch (e: Exception) {
                // handler error
                allMoviesLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun searchMovies(name:String) {
        viewModelScope.launch {
            searchMoviesLiveData.postValue(Resource.loading(null))
            try {
                val userFromDb = movieDao.searchMovie(name)
                searchMoviesLiveData.postValue(Resource.success(userFromDb))
            } catch (e: Exception) {
                // handler error
                searchMoviesLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun rateMovie(id:Int,rate:Float) {
        viewModelScope.launch {
            rateMovieLiveData.postValue(Resource.loading(null))
            try {
                movieDao.rateMovie(id,rate)
                rateMovieLiveData.postValue(Resource.success(1))
            } catch (e: Exception) {
                // handler error
                rateMovieLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

    fun editMovie(movie: Movie) {
        viewModelScope.launch {
            editMovieLiveData.postValue(Resource.loading(null))
            try {
                movieDao.update(movie)
                editMovieLiveData.postValue(Resource.success(1))
            } catch (e: Exception) {
                // handler error
                editMovieLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

}