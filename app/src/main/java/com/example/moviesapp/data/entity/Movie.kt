package com.example.moviesapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Movies")
data class Movie(
    var name: String,
    var details: String,
    var imageUrl: String,
    var year: String,
    var rate: Float,
    var category: String
):Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
