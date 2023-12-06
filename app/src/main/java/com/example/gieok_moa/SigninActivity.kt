package com.example.gieok_moa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gieok_moa.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        // 비밀번호 저장
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("password", password)
        editor.apply()

// 비밀번호 확인
        val sharedPref = getSharedPreferences("PasswordPreferences", Context.MODE_PRIVATE)
        val savedPassword = sharedPref.getString("password", "")
        if (inputPassword == savedPassword) {
            // 비밀번호 일치
        } else {
            // 비밀번호 불일치
        }

        // 앱이 백그라운드로 전환될 때 비밀번호 확인 액티비티 실행
        override fun onPause() {
            super.onPause()
            val intent = Intent(this, PasswordCheckActivity::class.java)
            startActivity(intent)
        }

 */


    }
}