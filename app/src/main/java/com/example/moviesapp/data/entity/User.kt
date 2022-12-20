package com.example.moviesapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Users")
data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
