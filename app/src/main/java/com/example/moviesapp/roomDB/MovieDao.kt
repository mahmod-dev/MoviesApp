package com.example.moviesapp.roomDB

import androidx.room.*
import com.example.moviesapp.data.entity.Movie

@Dao
interface MovieDao {

    @Insert
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Query("delete from movies where id =:id")
    suspend fun deleteMovie(id: Int)

    @Query("update movies SET rate=:rate where id =:id")
    suspend fun rateMovie(id: Int, rate: Float)

    @Query("select * from movies order by year desc")
    suspend fun getAllMovies(): List<Movie>

    @Query("select * from movies where name LIKE '%' || :name order by year desc")
    suspend fun searchMovie(name:String): List<Movie>
}