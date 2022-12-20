package com.example.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.entity.User
import com.example.moviesapp.roomDB.UserDao
import com.example.moviesapp.util.Resource
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDao: UserDao
) : ViewModel() {
    val insertUserLiveData = MutableLiveData<Resource<User>>()
    val updateUserLiveData = MutableLiveData<Resource<User>>()
    val loginLiveData = MutableLiveData<Resource<User>>()


     fun register(user: User) {
        viewModelScope.launch {
            insertUserLiveData.postValue(Resource.loading(null))
            try {
                userDao.insert(user)
                insertUserLiveData.postValue(Resource.success(user))
            } catch (e: Exception) {
                // handler error
                insertUserLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

     fun login(email: String, password: String) {
        viewModelScope.launch {
            loginLiveData.postValue(Resource.loading(null))
            try {
                val userFromDb = userDao.getUser(email, password)
                Log.e("TAG", "login: ${userFromDb}", )
                if (userFromDb!=null){
                    loginLiveData.postValue(Resource.success(userFromDb))
                }else{
                    loginLiveData.postValue(Resource.error("User not found!", null))

                }
            } catch (e: Exception) {
                // handler error
                loginLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

     fun editUser(user: User) {
        viewModelScope.launch {
            updateUserLiveData.postValue(Resource.loading(null))
            try {
                userDao.update(user)
                updateUserLiveData.postValue(Resource.success(user))
            } catch (e: Exception) {
                // handler error
                updateUserLiveData.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }

}