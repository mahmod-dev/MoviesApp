package com.example.moviesapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.data.entity.User
import com.example.moviesapp.databinding.FragmentProfileBinding
import com.example.moviesapp.util.Constant
import com.example.moviesapp.util.MyPreferences

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    lateinit var binding: FragmentProfileBinding
    lateinit var myPreferences: MyPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        myPreferences = MyPreferences(requireContext())
        binding.apply {
            val fName = myPreferences.getStr(Constant.FIRST_NAME)!!
            val lName = myPreferences.getStr(Constant.LAST_NAME)!!
            val email = myPreferences.getStr(Constant.EMAIL)!!
            val password = myPreferences.getStr(Constant.PASSWORD)!!
            val id = myPreferences.getInt(Constant.ID)


            tvName.text = "$fName $lName"
            tvEmail.text = myPreferences.getStr(Constant.EMAIL)

            imgEdit.setOnClickListener {
                val intent = Intent(requireContext(), SignUpActivity::class.java)
                val user = User(fName, lName, email, password)
                user.id = id
                intent.putExtra("user", user)
                startActivity(intent)
            }
            btnLogout.setOnClickListener {
                myPreferences.setBool(Constant.IS_LOGIN, false)
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
        }
    }
}