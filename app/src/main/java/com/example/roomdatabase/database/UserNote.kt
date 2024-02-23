package com.example.roomdatabase.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserNote(
    @PrimaryKey(autoGenerate = true) val uid :Int = 0,
    val title : String,
    val description : String
)

