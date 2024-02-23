package com.example.roomdatabase.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface Dao {
    @Insert
    fun insert(user: UserNote)

    @Update
    fun update(user: UserNote)

    @Delete
    fun delete(user: UserNote)

    @Query("delete from user_table")
    fun deleteAllNotes()

    @Query("select *from user_table")
    fun getAllNotes(): LiveData<List<UserNote>>

}