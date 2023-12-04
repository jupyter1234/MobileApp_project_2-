package com.example.gieok_moa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.util.Calendar
import java.util.Date

//필요한 query 정의 및 관계 정의
@Dao
interface userDao {
    @Query("SELECT * FROM user_table")
    fun getAll() : List<User>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: User)
    @Delete
    fun delete(user: User)
}

@Dao
interface snapDao {
    @Query("SELECT * FROM snap_table")
    fun getAll() : List<Snap>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg snap: Snap)
    @Delete
    fun delete(snap: Snap)


}
@Dao
interface tagDao {
    @Query("SELECT * FROM tag_table")
    fun getAll() : List<Tag>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tag: Tag)
    @Delete
    fun delete(tag: Tag)
}

@Dao
interface snapandTag{
    @Transaction
    @Query("SELECT * from snap_table")
    fun getSnapnTag() : List<snapandTag>

    @Transaction
    @Query("SELECT * from snap_table where createdDate between :a and :b")
    fun getSnapnTagbyTime(a : Long, b : Long) : List<snapandTag>

    fun getSnapandTagbyDate(c : Calendar) : List<snapandTag>{
        val startOfDay = c
        startOfDay.set(Calendar.HOUR_OF_DAY, 0)
        startOfDay.set(Calendar.MINUTE, 0)
        startOfDay.set(Calendar.SECOND, 0)
        startOfDay.set(Calendar.MILLISECOND, 0)
        val endOfDay = (startOfDay.clone() as Calendar)
        endOfDay.add(Calendar.HOUR_OF_DAY, 24)
        return getSnapnTagbyTime(startOfDay.time.time,endOfDay.time.time)
    }

    fun getSnapandTagbyDate(d : Date) : List<snapandTag> {
        val c = Calendar.getInstance()
        c.setTime(d)
        return getSnapandTagbyDate(c)
    }
}