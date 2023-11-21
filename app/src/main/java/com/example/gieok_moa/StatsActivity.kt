package com.example.gieok_moa

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
import java.util.Date


class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = UserDatabase.getInstance(applicationContext)

        deleteTemp(db)
        generateRandomSnap(db,10)
        generateTempTags(db,10)

        val moodScoreList = getScoreList(db)//tag가 각각 1,2,3 점일 때를 가진 List<Int> 반환

        val mood_chart = binding.chart

        configureChartAppearance(mood_chart)
        prepareChartData(mood_chart, createChartData(moodScoreList))


        //가장 많이 쓴 Tags가져오기
        val topTags = getTopTags(db,3)//time_created 1~5까지의 snap들에서 가장 많이 쓴 tags를 count만큼 가져옴

        binding.freq1stTag.setText("1st : ${topTags[0].key} (${topTags[0].value} times)")
        binding.freq2ndTag.setText("2nd : ${topTags[1].key} (${topTags[1].value} times)")
        binding.freq3rdTag.setText("3nd : ${topTags[2].key} (${topTags[2].value} times)")

    }

    fun getScoreList(db: UserDatabase?):List<Int> {
        val tag2List = db!!.tagDao().getAll()
        val colorList = mutableListOf<Int>(0,0,0)
        for(i in tag2List){
            when(i.color){
                com.example.gieok_moa.Color.RED->colorList[0]+=1
                com.example.gieok_moa.Color.YELLOW->colorList[1]+=1
                com.example.gieok_moa.Color.GREEN->colorList[2]+=1
            }
        }
        return colorList
    }

    fun getTopTags(db: UserDatabase?, count: Int):List<MutableMap.MutableEntry<String,Int>> {


        val tag2List = db!!.tagDao().getAll()

        val tagCountMap: MutableMap<String, Int> = HashMap()

        for (i in tag2List) {
                if (tagCountMap.containsKey(i.staus)) {
                    tagCountMap[i.staus] = tagCountMap[i.staus]!! + 1
                } else {
                    tagCountMap[i.staus] = 1
                }
        }
        val result: List<MutableMap.MutableEntry<String, Int>>
        if(tagCountMap.size<count) {
            result = tagCountMap.entries.sortedByDescending { it.value }.take(count)
        } else {
            result = tagCountMap.entries.sortedByDescending { it.value }
        }
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
                return value.toInt().toString()
            }
        }

        // 3. [BarData] 보여질 데이터 구성
        val data = BarData(set2)
        data.barWidth = 0.5f
        data.setValueTextSize(15f)

        return data
    }

    fun prepareChartData(barChart: HorizontalBarChart, data: BarData) {
        barChart.setData(data) // BarData 전달
        barChart.invalidate() // BarChart 갱신해 데이터 표시
    }
/*
    fun getTagsOfDay(db: SQLiteDatabase, date: Calendar):Int?{

// Set the start of the day (time = 00:00:00)
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        date.set(Calendar.MILLISECOND, 0)

// Get the start of the day in milliseconds
        val startOfDayInMillis = date.timeInMillis

// Set the end of the day (time = 23:59:59.999)
        date.set(Calendar.HOUR_OF_DAY, 23)
        date.set(Calendar.MINUTE, 59)
        date.set(Calendar.SECOND, 59)
        date.set(Calendar.MILLISECOND, 999)

// Get the end of the day in milliseconds
        val endOfDayInMillis = date.timeInMillis

        val cursor = db.rawQuery("select * from snap where created_time between $startOfDayInMillis and $endOfDayInMillis", null)
        val snap_ids = mutableListOf<Long>()
        while(cursor.moveToNext()){
            snap_ids.add(cursor.getLong(0))
        }
        if(snap_ids.size==0){
            return null
        }
        cursor.close()
        val colors = mutableListOf<Long>()
        for(i in snap_ids) {
            val cursor2 = db.rawQuery("select color from tags where snap_id = $i", null)
            colors.add(cursor2.getLong(0))
            cursor2.close()
        }
        var result = 0
        if(colors.average()>1.333){
            result = 2
        } else if(colors.average()>0.666){
            result = 1
        } else {
            result = 0
        }
        return result
    }

 */

    fun generateRandomSnap(db: UserDatabase?, count:Int) {
        for(i in 1..count) {
            val t = Snap(i.toLong(), Date(), "", "")
            db!!.snapDao().insertAll(t)
        }
    }

    fun generateTempTags(db: UserDatabase?, count:Int){
        for(i in 1..count){
            val state0 = when((1..4).random()){
                1->"t1"
                2->"t2"
                3->"t3"
                4->"t4"
                else->"t0"
            }
            db!!.tagDao().insertAll(Tag(i.toLong(), state0, com.example.gieok_moa.Color.RED, i.toLong()))
        }
    }

    fun deleteTemp(db: UserDatabase?){
        for(i in db!!.snapDao().getAll())
            db.snapDao().delete(i)
        for(i in db.tagDao().getAll())
            db.tagDao().delete(i)

    }
}