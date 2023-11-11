package com.example.gieok_moa

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.gieok_moa.databinding.ActivityMainBinding
import com.example.gieok_moa.databinding.SnapLayoutBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val snapdialogBinding = SnapLayoutBinding.inflate(layoutInflater)

        val dialog = Dialog(this)
        dialog.setContentView(snapdialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Dialog의 크기를 조절합니다. 여기서는 폭과 높이를 설정합니다.
        val width = resources.getDimensionPixelSize(R.dimen.dialog_width) // 원하는 폭의 크기를 설정하세요
        val height = resources.getDimensionPixelSize(R.dimen.dialog_height) // 원하는 높이의 크기를 설정하세요
        dialog.window?.setLayout(width, height)

        val setting=snapdialogBinding.root.findViewById<ImageView>(R.id.settingbutton)
        setting.setOnClickListener{
            //snap수정 창으로 이동
            dialog.dismiss()
        }
        val trash=snapdialogBinding.root.findViewById<ImageView>(R.id.trashbutton)
        trash.setOnClickListener {
            //삭제
            dialog.dismiss()
        }

        dialog.show()


    }
}