package com.example.gieok_moa

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.gieok_moa.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
// 비밀번호 확인
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        val savedPassword = sharedPreferences.getString("password", "")
        binding.pwSubmit.setOnClickListener {
            if (binding.pwInput.text.toString().equals(savedPassword)) {
                finish()
            } else {
                binding.wrongPwTextView.visibility = View.VISIBLE
            }
        }
    }

    private var doubleBackToExitPressedOnce = false
    private val mHandler: Handler = Handler()
    private val BACK_PRESS_DELAY = 3000L // 3 seconds


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()

        // Reset the flag after 3 seconds
        mHandler.postDelayed(Runnable { doubleBackToExitPressedOnce = false }, BACK_PRESS_DELAY)
    }
}