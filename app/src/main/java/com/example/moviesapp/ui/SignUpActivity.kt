package com.example.moviesapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapp.data.entity.User
import com.example.moviesapp.databinding.ActivitySignupBinding
import com.example.moviesapp.roomDB.MainDatabase
import com.example.moviesapp.util.*
import com.example.moviesapp.viewmodel.AuthViewModel
import com.example.moviesapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SignUpActivity : AppCompatActivity() {
    private val TAG = "SignUpActivity"
    lateinit var binding: ActivitySignupBinding
    lateinit var viewModel: AuthViewModel
    lateinit var myPreferences: MyPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        val user = intent.extras?.getSerializable("user") as User?
        observeRegisterUser()
        observeUpdateUser()
        binding.apply {
            if (user != null) {
                btnRegister.text = "Edit User"
                etFName.setText(user.firstName)
                etLastName.setText(user.lastName)
                etEmail.setText(user.email)
                etPassword.setText(user.password)
            }
            btnRegister.setOnClickListener {
                val fName = etFName.text.toString().trim()
                val lName = etLastName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    binding.root.rootView.showSnackbar("One of fields are empty!")
                } else if (password.length < 6) {
                    binding.root.rootView.showSnackbar("Password must be more than 6!")
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.root.rootView.showSnackbar("Please insert correct email!")
                } else {

                    if (user != null) {
                        user.firstName = fName
                        user.lastName = lName
                        user.email = email
                        user.password = password
                        viewModel.editUser(user)
                    } else {
                        viewModel.register(User(fName, lName, email, password))

                    }
                }

            }
        }
    }

    private fun observeRegisterUser() {
        viewModel.insertUserLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    myPreferences.setBool(Constant.IS_LOGIN, true)
                    myPreferences.saveUserInfo(it.data)

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    Log.e(TAG, "observeRegisterUser:SUCCESS ${it.data} ")
                }

                Status.LOADING -> {
                    Log.e(TAG, "observeRegisterUser:LOADING ")
                }
                Status.ERROR -> {
                    Log.e(TAG, "observeRegisterUser:ERROR ${it.message} ")
                }

            }
        }
    }

    private fun observeUpdateUser() {
        viewModel.updateUserLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    myPreferences.setBool(Constant.IS_LOGIN, true)
                    myPreferences.saveUserInfo(it.data)

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    Log.e(TAG, "observeRegisterUser:SUCCESS ${it.data} ")
                }

                Status.LOADING -> {
                    Log.e(TAG, "observeRegisterUser:LOADING ")
                }
                Status.ERROR -> {
                    Log.e(TAG, "observeRegisterUser:ERROR ${it.message} ")
                }

            }
        }
    }

    private fun init() {
        val roomDb = MainDatabase.getDatabase(this, CoroutineScope(SupervisorJob()))
        val factory = ViewModelFactory(userDao = roomDb.userDao())
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        myPreferences = MyPreferences(this)
    }
}