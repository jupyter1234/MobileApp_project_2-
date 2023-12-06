package com.example.gieok_moa

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.gieok_moa.databinding.AddSnapPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date

class AddSnapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= AddSnapPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateinit var datas: List<com.example.gieok_moa.Tag>
        lateinit var snapdatas: List<Snap>


        val currentContext: Context = this
        //db생성시 code
        val db = UserDatabase.getInstance(currentContext.applicationContext)
        val loading = CoroutineScope(Dispatchers.IO).launch {
            datas = db!!.tagDao().getAll()
        }
        runBlocking {
            loading.join()
        }

        //Log.d("park",datas[0].staus)
        var spinnerAdapterTag=TagSpinnerAdapter(this, R.layout.item_spinner, datas)

        binding.tagSpinner.adapter=spinnerAdapterTag
        Log.d("park","success3")

        binding.tagSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // 선택된 항목의 값 가져오기
                val selectedItem = parent.getItemAtPosition(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택되지 않았을 때의 동작
            }
        }
        binding.cancelButton.setOnClickListener {
            finish()
        }
        binding.completeButton.setOnClickListener {
            //database에 저장후 mainpage로 돌아감
            val loading = CoroutineScope(Dispatchers.IO).launch {
                snapdatas = db!!.snapDao().getAll()
            }
            runBlocking {
                loading.join()
            }

            val date= Date()
            val imageUri = "".toUri()
            var usercomment=binding.edittext.text.toString()
            if(usercomment==null) usercomment=""
            val loading2 = CoroutineScope(Dispatchers.IO).launch{
                val snap1 = Snap(0.toLong(), date, imageUri.toString(), usercomment)
                db!!.snapDao().insertAll(snap1)
                val snap_id = db!!.snapDao().getbyDate(date.time)
                val tag1 = Tag(snap_id[0].snapId, listOf<String>("t1","t2","t3","t4").random(),listOf<Color>(Color.RED,Color.YELLOW,Color.GREEN).random(), snap_id[0].snapId)
                db!!.tagDao().insertAll(tag1)
            }
            runBlocking {
                loading2.join()
            }//데이터 다 가져올 때 까지 wait

            Log.d("park",usercomment)
            finish()
        }
        binding.addtag.setOnClickListener {

        }
    }



}
