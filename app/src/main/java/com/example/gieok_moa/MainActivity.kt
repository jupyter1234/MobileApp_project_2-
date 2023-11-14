package com.example.gieok_moa

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gieok_moa.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewStatsButton.setOnClickListener{
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }
        binding.viewDailyStatsButton.setOnClickListener{
            val intent = Intent(this, DailyStatActivity::class.java)
            startActivity(intent)
        }
    }
}
public data class Snap(val snap_id :Long, var user_id: Long, val created_date: Long, val photo_url : String, val comment: String) {

    fun post(db: SQLiteDatabase) {
        db.execSQL("INSERT INTO snap (user_id, created_date, photo_url, comment) values ($user_id, $created_date, ?, ?)",
            arrayOf<String>(photo_url, comment)
        )
    }

    companion object {

        fun load(db: SQLiteDatabase, since: Long, till: Long): MutableList<Snap> {
            val cursor =
                db.rawQuery("select * from snap where time_created between $since and $till", null)
            var snapList = mutableListOf<Snap>()
            while (cursor.moveToNext()) {
                snapList.add(
                    Snap(
                        cursor.getLong(0),
                        cursor.getLong(1),
                        cursor.getLong(2),
                        cursor.getString(3),
                        cursor.getString(4)
                    )
                )
            }
            cursor.close()
            return snapList
        }

        fun generateSnapTable(db: SQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS snap")//나중에는 CREATE IF NOT EXISTS
            db.execSQL(
                "CREATE TABLE Snap (" +
                        "    snap_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    user_id LONG," +
                        "    created_date DATE," +
                        "    photo_url VARCHAR(255)," +
                        "    comment VARCHAR(255)" +
                        ");"
            )
        }

        fun generateUserTable(db: SQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS user")//나중에는 CREATE IF NOT EXISTS
            db.execSQL(
                "CREATE TABLE tag (" +
                        "    user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    name VARCHAR(255)" +
                        ")"
            )
        }

        fun generateTagTable(db: SQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS tag")//나중에는 CREATE IF NOT EXISTS
            db.execSQL(
                "CREATE TABLE tag (" +
                        "    tag_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    snap_id LONG," +
                        "    color LONG," +
                        "    state TEXT" +
                        ")"
            )

        }

        fun generateRandomSnap(db: SQLiteDatabase, count:Int) {
            for(i in 1..count) {
                val t = Snap(i.toLong(), 1, System.currentTimeMillis() + (-86400000..86400000).random(), "", "")
                t.post(db)
            }
        }
    }
}

public data class Tag(var tag_id:Long, var snap_id: Long, val color:Int, val state:String){


    companion object {
        fun load(db: SQLiteDatabase): MutableList<Tag> {
            val cursor =
                db.rawQuery("select * from tag", null)
            var tagList = mutableListOf<Tag>()
            while (cursor.moveToNext()) {
                tagList.add(
                    Tag(
                        cursor.getLong(0),
                        cursor.getLong(1),
                        cursor.getInt(2),
                        cursor.getString(3)
                    )
                )
            }
            cursor.close()
            return tagList
        }

        fun generateTempTags(db: SQLiteDatabase, count:Int){
            for(i in 1..count){
                val state0 = when((1..4).random()){
                    1->"t1"
                    2->"t2"
                    3->"t3"
                    4->"t4"
                    else->"t0"
                }
                Tag(i.toLong(),i.toLong(),(0..2).random(), state0).post(db)
            }
        }

    }
    fun post(db: SQLiteDatabase) {
        db.execSQL(
            "INSERT INTO tag (tag_id, snap_id, color, state) values ($tag_id, $snap_id, $color, ?)",
            arrayOf<String>(state)
        )
    }
    fun findSnapById(db: SQLiteDatabase):Snap{
        val cursor = db.rawQuery("select * from snap where snap_id = $snap_id", null)
        return Snap(
            cursor.getLong(0),
            cursor.getLong(1),
            cursor.getLong(2),
            cursor.getString(3),
            cursor.getString(4)
        )
        cursor.close()
    }
}

