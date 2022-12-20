package com.example.moviesapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentMovieDetailsBinding
import com.example.moviesapp.roomDB.MainDatabase
import com.example.moviesapp.util.Status
import com.example.moviesapp.viewmodel.MovieViewModel
import com.example.moviesapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    private val TAG = "MovieDetailsFragment"
    lateinit var binding: FragmentMovieDetailsBinding
    val args: MovieDetailsFragmentArgs by navArgs()
    lateinit var viewModel: MovieViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)
        val movie = args.movie
        init()
        rateMovie()
        binding.apply {

            tvMovieName.text = "Movie name: ${movie.name}"
            tvYear.text = "Publish year: ${movie.year}"
            tvCategory.text = "Category: ${movie.category}"
            rateBar.rating = movie.rate
            rating(rateBar.rating)

            Glide.with(requireContext())
                .load(movie.imageUrl)
                .into(imagePoster)


            rateBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                rating(rating)
                viewModel.rateMovie(movie.id, rating)
            }
        }


    }

    private fun rateMovie() {
        viewModel.rateMovieLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.e(TAG, "rateMovie:SUCCESS ${it.data} ")
                }

                Status.LOADING -> {
                    Log.e(TAG, "rateMovie:LOADING ")
                }
                Status.ERROR -> {
                    Log.e(TAG, "rateMovie:ERROR ${it.message} ")
                }
            }
        }
    }

    private fun init() {
        val roomDb = MainDatabase.getDatabase(requireContext(), CoroutineScope(SupervisorJob()))
        val factory = ViewModelFactory(movieDao = roomDb.movieDao())
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
    }

    private fun rating(rating: Float) {
        val noofstars: Int = binding.rateBar.numStars
        val getrating: Float = rating
        binding.tvRate.text = "Rating: $getrating/$noofstars"
    }
}