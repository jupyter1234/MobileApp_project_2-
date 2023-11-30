package com.example.gieok_moa

import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gieok_moa.databinding.FragmentMainBinding
import com.example.gieok_moa.databinding.ItemSnapBinding
import com.example.gieok_moa.databinding.SelectionDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.text.SimpleDateFormat
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        // loading current date
        val currentDate = SimpleDateFormat("MM/dd").format(Date())
        binding.date.text = currentDate

        binding.settingButton.setOnClickListener {
            //Log.d("check", "setting button clicked")
//            val intent: Intent = Intent(this, oooActivity::class.java)
//            startActivity(intent)
        }
        binding.calendarButton.setOnClickListener {
            //Log.d("check", "calendar button clicked")
            //val intent: Intent = Intent(this, oooActivity::class.java)
            //startActivity(intent)
        }

        val db = UserDatabase.getInstance(requireContext().applicationContext)


        var datas: MutableList<Snap> = mutableListOf()
        val loading = CoroutineScope(Dispatchers.IO).launch {
            datas = db!!.snapDao().getAll().toMutableList()
        }

        deleteTemps()
        val imageUrl = "android.resource://com.example.gieok_moa/drawable/snap_add_button"
        val snapAddButton: Snap = Snap(0, Date(), imageUrl)
        val snapEx = Snap(1, Date(),
            "https://images.squarespace-cdn.com/content/v1/56840d91e0327c52f60c392f/1452044547091-64DRLH2A3XIW3Z6GUQHF/GOOD_Logo.jpg",
            "good")
        datas.add(snapEx)
        datas.add(snapAddButton)
        // add(MyAdapter.size, snap1)

        // insert data to database
//        CoroutineScope(Dispatchers.IO).launch {
//            val i = 0
//            val imageUri = "".toUri()
//            val snap1 = Snap(i.toLong(), Date(), imageUri.toString(), "")
//            db!!.snapDao().insertAll(snap1)
//        }

        // 데이터 정렬 함수 추가

//        datas.add(snapEx)
//        datas.add(snapEx)
//        datas.add(snapEx)
//        datas.add(snapEx)

        val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager

        val adapter = MyAdapter(datas){ snap ->
            //Log.d("check", "snap clicked")
            if (snap.photoUrl == imageUrl){
                // when snap add button clicked -> popup & move to snap add page
                // selection dialog
                val selectionDialog = SelectionDialogBinding.inflate(layoutInflater)
                AlertDialog.Builder(requireActivity()).run {
                    setView(selectionDialog.root)
                    selectionDialog.takePic.setOnClickListener {
                        // move to Camera App
                        Log.d("test", "take a picture")
                    }
                    selectionDialog.fromGal.setOnClickListener {
                        // move to Gallery App
                        Log.d("test", "move to gallery")
                    }
                    show()
                }
            }
            else{
                // when snap clicked -> dialog
                
            }
        }
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    //DB에 Snap저장, 지금은 무작위 tag도 만들어서 tag도 저장
    private fun storeImageUriInRoomDatabase(imageUri: Uri) {
        val db = UserDatabase.getInstance(requireContext().applicationContext)

        CoroutineScope(Dispatchers.IO).launch {
            for(i in 1..10){
                val snap1 = Snap(i.toLong(), Date(), imageUri.toString(), "")
                val tag1 = Tag(i.toLong(), arrayOf("t1","t2","t3","t4").random(), arrayOf(Color.GREEN,Color.RED,Color.YELLOW).random(), i.toLong())
                db!!.snapDao().insertAll(snap1)
                db.tagDao().insertAll(tag1)
            }
        }
    }
    // delete all
    fun deleteTemps(){
        val db = UserDatabase.getInstance(requireContext().applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            for(i in db!!.snapDao().getAll()) {
                db.snapDao().delete(i)
            }
            for(i in db.tagDao().getAll()) {
                db.tagDao().delete(i)
            }
        }
    }

}