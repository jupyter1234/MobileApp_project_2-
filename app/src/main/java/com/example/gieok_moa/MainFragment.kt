package com.example.gieok_moa

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gieok_moa.databinding.FragmentMainBinding
import com.example.gieok_moa.databinding.SelectionDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.text.SimpleDateFormat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
//import com.example.gieok_moa.databinding.SnapLayoutBinding
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import android.graphics.Color
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.gieok_moa.databinding.AddSnapPageBinding
import com.example.gieok_moa.databinding.SnapLayoutBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class MainFragment : Fragment() {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val REQUEST_IMAGE_CAPTURE = 1

    private val PICK_IMAGE_REQUEST = 1  // 갤러리에서 이미지를 선택하는 요청 코드
    private lateinit var binding: FragmentMainBinding // 바인딩 클래스 인스턴스

    private lateinit var datas: MutableList<Snap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
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
            val intent: Intent = Intent(activity, MainCalendar::class.java)
            startActivity(intent)
        }

        // db 불러오기
        val db = UserDatabase.getInstance(requireContext().applicationContext)
        datas = mutableListOf()
        val loading = CoroutineScope(Dispatchers.IO).launch {
            datas = db!!.snapDao().getAll().toMutableList()
        }
        runBlocking {
            loading.join()
        }

        val snapAddImageUrl = "android.resource://com.example.gieok_moa/drawable/snap_add_button1"
        val snapAddButton: Snap = Snap(0.toLong(), Date(), snapAddImageUrl, "")
        datas.add(snapAddButton)

        val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager

        val adapter = MyAdapter(datas){ snap ->
            //Log.d("check", "snap clicked")
            if (snap.photoUrl == snapAddImageUrl){
                // when snap add button clicked -> popup & move to snap add page
                // selection dialog
                val selectionDialog = SelectionDialogBinding.inflate(layoutInflater)
                AlertDialog.Builder(requireActivity()).run {
                    setView(selectionDialog.root)
                    selectionDialog.takePic.setOnClickListener {
                        // move to Camera App
                        //dispatchTakePictureIntent()

                        val intent1=Intent(activity, AddSnapActivity::class.java)
                        startActivity(intent1)
                    }
                    selectionDialog.fromGal.setOnClickListener {
                        // move to Gallery App
                        Log.d("park","setonclicker")
                        openGallery()

                    }
                    show()
                }
            }
            else{
                // when snap clicked -> dialog
                val snapdialogBinding = SnapLayoutBinding.inflate(layoutInflater)

                val dialog = Dialog(requireContext().applicationContext)
                dialog.setContentView(snapdialogBinding.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                // Dialog의 크기를 조절합니다. 여기서는 폭과 높이를 설정합니다.
                val width = resources.getDimensionPixelSize(R.dimen.dialog_width) // 원하는 폭의 크기를 설정하세요
                val height = resources.getDimensionPixelSize(R.dimen.dialog_height) // 원하는 높이의 크기를 설정하세요
                dialog.window?.setLayout(width, height)

                val setting=snapdialogBinding.root.findViewById<ImageView>(R.id.settingbutton)
                setting.setOnClickListener{
                    //snap수정 창으로 이동
                    Log.d("ko", "setting button")
                    dialog.dismiss()
                }
                val trash=snapdialogBinding.root.findViewById<ImageView>(R.id.trashbutton)
                trash.setOnClickListener {
                    //삭제
                    Log.d("ko", "setting button")
                    db!!.snapDao().delete(snap)
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    private var listener: OnSnapAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSnapAddedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnAddSnapClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        //deleteTemps()
    }

    companion object {
        lateinit var imageUri: Uri
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // 사용자가 이미지를 선택한 후의 처리
        if (uri != null) {
            Log.d("park", "pick picture")
            imageUri=uri
            Log.d("park","Main ${uri.toString()}")


            val intent1=Intent(activity, AddSnapActivity::class.java)
            startActivity(intent1)
        }
    }


    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }








    //2
    // 카메라 관련 코드
//    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        }
//    }

//    override fun onAcrtivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            // Handle the captured image, e.g., save its URL to Room database
//            val photoUri = data?.data
//            savePhotoToDatabase(photoUri.toString())
//
//            // Return to the previous app
//            //finish()
//        }
//    }

    private fun savePhotoToDatabase(photoUrl: String) {
        // Implement your Room database insertion logic here
        // You need to have a Room database set up and a data class representing your entity
        // For simplicity, I'm assuming you have a PhotoEntity class with a DAO
        val db = UserDatabase.getInstance(requireContext().applicationContext)
        val snap = Snap(5L, Date(), photoUrl, "")
        CoroutineScope(Dispatchers.IO).launch {
            db!!.snapDao().insertAll(snap)
        }
    }

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

interface OnSnapAddedListener {
    fun onSnapAdded()
}