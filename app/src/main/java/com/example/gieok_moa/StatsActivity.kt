package com.example.gieok_moa

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gieok_moa.databinding.ActivityStatsBinding


class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = openOrCreateDatabase("testdb", Context.MODE_PRIVATE,null)

        db.execSQL("DROP TABLE IF EXISTS snap")
        db.execSQL("CREATE TABLE snap (" +
                "date_created INTEGER," +
                "image_address TEXT," +
                "description TEXT," +
                "mood_score INTEGER," +
                "tags TEXT," +
                "primary key(date_created))"
        )
        db.execSQL("INSERT INTO snap (date_created, image_address, description, mood_score, tags) values (1, ?, ?, 1, ?)",arrayOf<String>("i1","d1","t1"))
        db.execSQL("INSERT INTO snap (date_created, image_address, description, mood_score, tags) values (2, ?, ?, 2, ?)",arrayOf<String>("i2","d2","t2,t1"))
        db.execSQL("INSERT INTO snap (date_created, image_address, description, mood_score, tags) values (3, ?, ?, 3, ?)",arrayOf<String>("i3","d3","t3,t2,t1"))
        db.execSQL("INSERT INTO snap (date_created, image_address, description, mood_score, tags) values (4, ?, ?, 4, ?)",arrayOf<String>("i4","d4","t4,t3,t2,t1"))
        db.execSQL("INSERT INTO snap (date_created, image_address, description, mood_score, tags) values (5, ?, ?, 6, ?)",arrayOf<String>("i5","d5","t1,t3,t5"))

        val cursor = db.rawQuery("select * from snap", null)
        var sum_mood_score = 0.0
        var rows =0



        val tagCountMap: MutableMap<String, Int> = HashMap()


        var i = 0
        while(cursor.moveToNext()) {
            val c1 = cursor.getInt(0)
            val c2 = cursor.getString(1)
            val c3 = cursor.getString(2)
            val c4 = cursor.getInt(3)
            val c5 = cursor.getString(4)
            sum_mood_score += c4.toDouble()
            rows += 1
            val splitStr = c5.split(",")
            for (i in splitStr.indices) {
                if (tagCountMap.containsKey(splitStr[i])) {
                    tagCountMap[splitStr[i]] = tagCountMap[splitStr[i]]!! + 1
                } else {
                    tagCountMap[splitStr[i]] = 1
                }
            }
        }

        binding.averageMoodScore.setText("${sum_mood_score/rows}")
        cursor.close()

        val topKeys = getTopKeys(tagCountMap, 3)
        binding.freq1stTag.setText("1st : ${topKeys[0]}, (${tagCountMap[topKeys[0]]} times)")
        binding.freq2ndTag.setText("2nd : ${topKeys[1]}, (${tagCountMap[topKeys[1]]} times)")
        binding.freq3rdTag.setText("3nd : ${topKeys[2]}, (${tagCountMap[topKeys[2]]} times)")

    }

    fun getTopKeys(map: Map<String, Int>, count: Int): List<String> {
        val topKeys = mutableListOf<String>()

        // Map을 값에 따라 내림차순으로 정렬하고 상위 count 개수만큼의 key를 추출
        map.entries.sortedByDescending { it.value }
            .take(count)
            .mapTo(topKeys) { it.key }

        return topKeys
    }
}