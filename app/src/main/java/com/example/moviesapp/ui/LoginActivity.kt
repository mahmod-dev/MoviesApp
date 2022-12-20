package com.example.moviesapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapp.databinding.ActivityLoginBinding
import com.example.moviesapp.roomDB.MainDatabase
import com.example.moviesapp.util.*
import com.example.moviesapp.viewmodel.AuthViewModel
import com.example.moviesapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: AuthViewModel
    lateinit var myPreferences: MyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val roomDb = MainDatabase.getDatabase(this, CoroutineScope(SupervisorJob()))
        val factory = ViewModelFactory(userDao = roomDb.userDao())
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        myPreferences = MyPreferences(this)

        observeLogin()
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    binding.root.rootView.showSnackbar("One of fields are empty!")
                } else if (password.length < 6) {
                    binding.root.rootView.showSnackbar("Password must be more than 6!")
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.root.rootView.showSnackbar("Please insert correct email!")
                } else {
                    viewModel.login(email, password)
                }
            }

            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun observeLogin() {
        viewModel.loginLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.e(TAG, "observeLogin:SUCCESS ${it.data} ")
                    myPreferences.setBool(Constant.IS_LOGIN, true)
                    myPreferences.saveUserInfo(it.data)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                Status.LOADING -> {
                    Log.e(TAG, "observeLogin:LOADING ")
                }
                Status.ERROR -> {
                    binding.root.rootView.showSnackbar("Email or password is wrong!")
                    Log.e(TAG, "observeLogin:ERROR ${it.message} ")
                }

            }
        }
    }
}