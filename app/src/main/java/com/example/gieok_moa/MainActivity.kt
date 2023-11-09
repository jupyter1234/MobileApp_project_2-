package com.example.gieok_moa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gieok_moa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val snapdialogBinding=
    }
}