package com.example.gieok_moa

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.gieok_moa.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    lateinit var datas: List<com.example.gieok_moa.Tag>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("park","success")

        val currentContext: Context = this

        val db = UserDatabase.getInstance(currentContext.applicationContext)
        Log.d("park","success2")
        CoroutineScope(Dispatchers.IO).launch {
            for(i in 1..10){
                val tag1 = Tag(i.toLong(), arrayOf("t1","t2","t3","t4").random(), arrayOf(Color.GREEN,Color.RED,Color.YELLOW).random(), i.toString())
                db!!.tagDao().insertAll(tag1)
            }
        }
        Log.d("park","success3")
        /*val i = 0
        val st="행복"
        val color=Color.RED
        val tag1 = com.example.gieok_moa.Tag(i.toLong(), st,color, "1")

        datas = mutableListOf()
        datas.add(tag1)*/
        val loading = CoroutineScope(Dispatchers.IO).launch {
            datas = db!!.tagDao().getAll()
        }
        runBlocking {
            loading.join()
        }
        //Log.d("park",datas[0].staus)
        var spinnerAdapterTag=TagSpinnerAdapter(this, R.layout.item_spinner, datas)

        binding.tagSpinner.adapter=spinnerAdapterTag
        binding.cancelButton.setOnClickListener {
            //finish()
        }
        binding.completeButton.setOnClickListener {
            //database에 저장후 mainpage로 돌아감
            //finish()
        }
        binding.addtag.setOnClickListener {

        }
    }

}