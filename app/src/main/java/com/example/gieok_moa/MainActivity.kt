package com.example.gieok_moa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.gieok_moa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        binding.addimage.setOnClickListener {
            // 갤러리에서 이미지를 선택하는 인텐트를 생성
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
        }

        binding.cancelButton.setOnClickListener {

            //main 페이지로 돌아감
        }
        binding.completeButton.setOnClickListener {
            //database에 저장후 mainpage로 돌아감
        }*/
    }
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // 선택한 이미지의 URI를 가져옴
            val imageUri = data.data

            // URI를 이용하여 이미지를 설정
            binding.addimage.setImageURI(imageUri)
        }
    }*/
}