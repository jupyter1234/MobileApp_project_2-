package com.example.gieok_moa

import android.nfc.Tag
import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.gieok_moa.databinding.ActivityMainBinding

class AddSnapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //스피너 객체 생성
        val spinnerTag: Spinner =findViewById(R.id.tag_spinner)

        //리스트 생성
        val tagList: ArrayList<Tag> = ArrayList<Tag>()

        //데이터 생성

        //데이터 리스트에 담기

        //어댑터 생성
        //val adapter: TagAdapter=TagAdapter(this, tagList)

        //어댑터 적용
        //spinnerTag.adapter=adapter
        binding.cancelButton.setOnClickListener {
            finish()
        }
        binding.completeButton.setOnClickListener {
            //database에 저장후 mainpage로 돌아감
            finish()
        }
    }
}