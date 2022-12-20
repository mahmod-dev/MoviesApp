package com.example.moviesapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.Categories
import com.example.moviesapp.data.entity.Movie
import com.example.moviesapp.databinding.FragmentAddMovieBinding
import com.example.moviesapp.roomDB.MainDatabase
import com.example.moviesapp.util.Status
import com.example.moviesapp.util.bitmapToFile
import com.example.moviesapp.util.spinnerArr
import com.example.moviesapp.util.toBitmap
import com.example.moviesapp.viewmodel.MovieViewModel
import com.example.moviesapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class AddMovieFragment : Fragment(R.layout.fragment_add_movie) {
    lateinit var binding: FragmentAddMovieBinding
    val args: AddMovieFragmentArgs by navArgs()
    lateinit var viewModel: MovieViewModel
    private val TAG = "AddMovieFragment"
    var isAdd = true
    var imgPath: String? = null
    var selectedCategory = Categories.ACTION.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddMovieBinding.bind(view)
        init()
        initSpinner()
        isAdd = args.movie == null

        binding.apply {
            if (isAdd) {
                selectedCategory = spinner.selectedItem.toString()
                Log.e(TAG, "onItemSelected: $selectedCategory")
                btnAdd.text = "Add Movie"
            } else {
                btnAdd.text = "Edit Movie"
                etName.setText(args.movie?.name)
                etDesc.setText(args.movie?.details)
                etYear.setText(args.movie?.year)
                val index = spinnerArr().indexOf(args.movie?.category)
                spinner.setSelection(index)
                selectedCategory = args.movie?.category.toString()

                Glide.with(requireContext())
                    .load(args.movie?.imageUrl)
                    .into(img)



            }
            img.setOnClickListener {
                imageChooser()
            }

            btnAdd.setOnClickListener {

                val name = etName.text.toString()
                val desc = etDesc.text.toString()
                val year = etYear.text.toString()


                if (isAdd) {
                    if (imgPath == null) {
                        Toast.makeText(requireContext(), "Please Add Image", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    val movie = Movie(
                        name = name,
                        details = desc,
                        year = year,
                        category = selectedCategory,
                        rate = 0f,
                        imageUrl = imgPath!!,
                    )
                    viewModel.addMovie(movie)
                } else {
                    //edit
                    args.movie?.name = name
                    args.movie?.details = desc
                    args.movie?.year = year
                    args.movie?.category = selectedCategory

                    if (imgPath != null) {
                        args.movie?.imageUrl = imgPath!!
                    }
                    viewModel.editMovie(args.movie!!)
                }
            }
        }

    }

    private fun init() {
        val roomDb = MainDatabase.getDatabase(requireContext(), CoroutineScope(SupervisorJob()))
        val factory = ViewModelFactory(movieDao = roomDb.movieDao())
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
        fetchAddMovies()
        fetchEditMovies()
    }


    private fun fetchAddMovies() {
        viewModel.insertMovieLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e(TAG, "fetchMovies:SUCCESS ${it.data} ")
                    restartMainActivity()
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.e(TAG, "fetchMovies:LOADING ")
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e(TAG, "fetchMovies:ERROR ${it.message} ")
                }
            }
        }
    }

    private fun fetchEditMovies() {
        viewModel.editMovieLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    restartMainActivity()
                    Log.e(TAG, "fetchEditMovies:SUCCESS ${it.data} ")
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE

                    Log.e(TAG, "fetchEditMovies:LOADING ")
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE

                    Log.e(TAG, "fetchEditMovies:ERROR ${it.message} ")
                }
            }
        }
    }

    private fun initSpinner() {
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item,
            spinnerArr()
        )

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = spinnerArrayAdapter


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                selectedCategory = selectedItem
                Log.e(TAG, "onItemSelected: $selectedCategory")
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }


    private fun restartMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        // i.type = "*/*"
        i.action = Intent.ACTION_OPEN_DOCUMENT
        i.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "application/*"))
        resultLauncher.launch(Intent.createChooser(i, "Select Media"))
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fileUri = result.data?.data

                val bitmap = fileUri?.toBitmap(requireActivity().contentResolver)
                imgPath = bitmapToFile(bitmap)?.path
                binding.img.setImageBitmap(bitmap)
            }
        }

}