package com.example.gieok_moa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
//필요한 query 정의 및 관계 정의
@Dao
interface userDao {
    @Query("SELECT * FROM user_table")
    fun getAll() : List<User>
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: User)
    @Delete
    fun delete(user: User)
}

@Dao
interface snapDao {
    @Query("SELECT * FROM snap_table")
    fun getAll() : List<Snap>
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertAll(vararg snap: Snap)
    @Delete
    fun delete(snap: Snap)
    @Query("DELETE FROM snap_table")
    fun deleteAll()
}
@Dao
interface tagDao {
    @Query("SELECT * FROM tag_table")
    fun getAll() : List<Tag>
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertAll(vararg tag: Tag)
    @Delete
    fun delete(tag: Tag)
}