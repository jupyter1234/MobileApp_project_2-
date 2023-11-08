package com.example.gieok_moa

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gieok_moa.databinding.ActivityStatsBinding
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter


class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = openOrCreateDatabase("testdb", Context.MODE_PRIVATE, null)

        getTempTable(db)//make temp db table
        val since = 1
        val till = 5
        val moodScoreList = getScoreList(db,since,till)//time_created 1~5까지의 snap들에서 moodscore가 각각 1,2,3 점일 때를 가진 List<Int> 반환
        val avrg = (moodScoreList[0]*1 + moodScoreList[1]*2 + moodScoreList[2]*3).toDouble() / (till-since +1)
        binding.averageMoodScore.setText("Mood Score : $avrg")

        val mood_chart = binding.chart

        configureChartAppearance(mood_chart)
        prepareChartData(mood_chart, createChartData(moodScoreList))


        //가장 많이 쓴 Tags가져오기
        val topTags = getTopTags(db,3,since,till)//time_created 1~5까지의 snap들에서 가장 많이 쓴 tags를 count만큼 가져옴

        binding.freq1stTag.setText("1st : ${topTags[0].key} (${topTags[0].value} times)")
        binding.freq2ndTag.setText("2nd : ${topTags[1].key} (${topTags[1].value} times)")
        binding.freq3rdTag.setText("3nd : ${topTags[2].key} (${topTags[2].value} times)")



    }

    fun getTempTable(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS snap")//나중에는 CREATE IF NOT EXISTS
        db.execSQL(
            "CREATE TABLE snap (" +
                    "time_created INTEGER," +
                    "image_address TEXT," +
                    "description TEXT," +
                    "mood_score INTEGER," +
                    "tags TEXT," +
                    "primary key(time_created))"
        )
        db.execSQL(
            "INSERT INTO snap (time_created, image_address, description, mood_score, tags) values (1, ?, ?, 1, ?)",
            arrayOf<String>("i1", "d1", "t1")
        )
        db.execSQL(
            "INSERT INTO snap (time_created, image_address, description, mood_score, tags) values (2, ?, ?, 2, ?)",
            arrayOf<String>("i2", "d2", "t2,t1")
        )
        db.execSQL(
            "INSERT INTO snap (time_created, image_address, description, mood_score, tags) values (3, ?, ?, 3, ?)",
            arrayOf<String>("i3", "d3", "t3,t2,t1")
        )
        db.execSQL(
            "INSERT INTO snap (time_created, image_address, description, mood_score, tags) values (4, ?, ?, 2, ?)",
            arrayOf<String>("i4", "d4", "t4,t3,t2,t1")
        )
        db.execSQL(
            "INSERT INTO snap (time_created, image_address, description, mood_score, tags) values (5, ?, ?, 1, ?)",
            arrayOf<String>("i5", "d5", "t1,t3,t5")
        )

    }

    fun getScoreList(db: SQLiteDatabase, since: Int, till: Int):List<Int> {
        val cursor = db.rawQuery("select mood_score, count(*) from snap where time_created between $since and $till group by mood_score order by mood_score", null)
        val moodScoreArray = mutableListOf<Int>(0,0,0)
        for(i in 0..2){
            cursor.moveToNext()
            moodScoreArray.set(i, cursor.getInt(1))
        }
        cursor.close()
        return moodScoreArray
    }

    fun getTopTags(db: SQLiteDatabase, count: Int, since: Int, till: Int):List<MutableMap.MutableEntry<String,Int>> {

        //tags가 count보다 적을 때 예외처리 해야 함
        val cursor = db.rawQuery("select tags from snap where time_created between $since and $till", null)

        val tagCountMap: MutableMap<String, Int> = HashMap()

        while (cursor.moveToNext()) {
            val splitStr = cursor.getString(0).split(",")
            for (i in splitStr.indices) {
                if (tagCountMap.containsKey(splitStr[i])) {
                    tagCountMap[splitStr[i]] = tagCountMap[splitStr[i]]!! + 1
                } else {
                    tagCountMap[splitStr[i]] = 1
                }
            }
        }
        cursor.close()

        val result = tagCountMap.entries.sortedByDescending { it.value }.take(count)
        return result
    }

    fun configureChartAppearance(barChart: HorizontalBarChart) {
        barChart.setDrawGridBackground(false)
        barChart.getDescription().setEnabled(false) // chart 밑에 description 표시 유무
        barChart.setTouchEnabled(false) // 터치 유무
        barChart.getLegend().setEnabled(false) // Legend는 차트의 범례
        barChart.setExtraOffsets(0f, 0f, 5f, 0f)

        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        val xAxis: XAxis = barChart.getXAxis()
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.textSize = 15f
        xAxis.gridLineWidth = 25f
        xAxis.gridColor = Color.parseColor("#00000000")
        xAxis.position = XAxis.XAxisPosition.BOTTOM // X 축 데이터 표시 위치

        // YAxis(Left) (수평 막대 기준 아래쪽) - 선 유무, 데이터 최솟값/최댓값, label 유무
        val axisLeft: YAxis = barChart.getAxisLeft()
        axisLeft.setDrawGridLines(false)
        axisLeft.setDrawAxisLine(false)
        axisLeft.axisMinimum = 0f // 최솟값
        //axisLeft.axisMaximum = 5f // 최댓값
        axisLeft.resetAxisMaximum()
        axisLeft.granularity = 1f // 값만큼 라인선 설정
        axisLeft.setDrawLabels(false) // label 삭제

        // YAxis(Right) (수평 막대 기준 위쪽) - 사이즈, 선 유무
        val axisRight: YAxis = barChart.getAxisRight()
        axisRight.textSize = 15f
        axisRight.setDrawLabels(false) // label 삭제
        axisRight.setDrawGridLines(false)
        axisRight.setDrawAxisLine(false)

        // XAxis에 원하는 String 설정하기 (앱 이름)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when(value){
                    0f -> "Good"
                    1F -> "Soso"
                    2f -> "bad"
                    else -> ""
                }

            }
        }
    }

    fun createChartData(inputData:List<Int>): BarData {

        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        val values = ArrayList<BarEntry>()
        var t = 0.0f
        for (i in inputData) {
            val x = t
            t += 1.0f
            val y = i.toFloat()
            values.add(BarEntry(x, y))
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀
        val set2 = BarDataSet(values, "엄")
        set2.setDrawIcons(false)
        set2.setDrawValues(true)
        set2.setColors(
            Color.rgb(0, 255, 0), Color.rgb(255, 255, 0), Color.rgb(255, 0, 0))
        set2.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString() + "회"
            }
        }

        // 3. [BarData] 보여질 데이터 구성
        val data = BarData(set2)
        data.barWidth = 0.5f
        data.setValueTextSize(15f)

        return data


    }

    private fun prepareChartData(barChart: HorizontalBarChart, data: BarData) {
        barChart.setData(data) // BarData 전달
        barChart.invalidate() // BarChart 갱신해 데이터 표시
    }
}