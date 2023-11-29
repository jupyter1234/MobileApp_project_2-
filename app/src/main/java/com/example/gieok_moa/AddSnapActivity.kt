/*
package com.example.gieok_moa

import android.nfc.Tag
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.room.PrimaryKey
import com.example.gieok_moa.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.FieldPosition

class AddSnapActivity : AppCompatActivity() {

   */
/* val db = UserDatabase.getInstance(this.applicationContext)
    lateinit var datas: List<com.example.gieok_moa.Tag>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            val i = 0
            val st="행복"
            val color=Color.RED
            val tag1 = com.example.gieok_moa.Tag(i.toLong(), st,color)
            db!!.tagDao().insertAll(tag1)
        }

        val loading = CoroutineScope(Dispatchers.IO).launch {
            datas = db!!.tagDao().getAll()
        }

        var spinnerAdapterTag=TagSpinnerAdapter(this, R.layout.item_spinner, datas)

        binding.tagSpinner.adapter=spinnerAdapterTag
*//*
*/
/*
        //스피너 객체 생성
        val spinnerTag: Spinner =findViewById(R.id.tag_spinner)

        //리스트 생성
        val tagList: ArrayList<Tag> = ArrayList<Tag>()

        //데이터 생성

        //데이터 리스트에 담기

        //어댑터 생성
        val adapter: TagSpinnerAdapter=TagAdapter(this, tagList)

        //어댑터 적용
        //spinnerTag.adapter=adapter*//*
*/
/*
        binding.cancelButton.setOnClickListener {
            finish()
        }
        binding.completeButton.setOnClickListener {
            //database에 저장후 mainpage로 돌아감
            finish()
        }
        binding.addtag.setOnClickListener {

        }
    }*//*



}*/
