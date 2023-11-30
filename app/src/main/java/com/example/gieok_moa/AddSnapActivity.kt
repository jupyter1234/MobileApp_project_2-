package com.example.gieok_moa

import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.room.PrimaryKey
import com.example.gieok_moa.databinding.ActivityMainBinding
import com.example.gieok_moa.databinding.AddSnapPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.FieldPosition
import java.util.Date

class AddSnapActivity : AppCompatActivity() {

    lateinit var datas: List<com.example.gieok_moa.Tag>
    lateinit var snapdatas: List<Snap>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= AddSnapPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

            val snapid=snapdatas[snapdatas.size-1].snapId+1
            CoroutineScope(Dispatchers.IO).launch{
                val snap1 = Snap(snapid.toLong(), date, imageUri.toString(), usercomment)
                db!!.snapDao().insertAll(snap1)
            }
            Log.d("park",usercomment)
            finish()
        }
        binding.addtag.setOnClickListener {

        }
    }



}
