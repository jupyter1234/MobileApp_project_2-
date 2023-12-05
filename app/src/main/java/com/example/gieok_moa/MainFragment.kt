package com.example.gieok_moa

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import com.example.gieok_moa.databinding.SnapLayoutBinding
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import android.graphics.Color
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat

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
            val intent: Intent = Intent(activity, MainCalendar::class.java)
            startActivity(intent)
        }

        // db 불러오기
        val db = UserDatabase.getInstance(requireContext().applicationContext)
        var datas: MutableList<Snap> = mutableListOf()
        val loading = CoroutineScope(Dispatchers.IO).launch {
            datas = db!!.snapDao().getAll().toMutableList()
        }
        runBlocking {
            loading.join()
        }

        val snapAddImageUrl = "android.resource://com.example.gieok_moa/drawable/snap_add_button1"
        val snapAddButton: Snap = Snap(0.toLong(), Date(), snapAddImageUrl, "")
        //datas.add(snapAddButton)
        // insert "add snap button" to database
        CoroutineScope(Dispatchers.IO).launch {
            db!!.snapDao().insertAll(snapAddButton)
        }

        // 데이터 정렬 함수 추가

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
                        Log.d("test", "take a picture")
                        //dispatchTakePictureIntent()
                        
                        val intent1=Intent(activity, AddSnapActivity::class.java)
                        startActivity(intent1)
                    }
                    selectionDialog.fromGal.setOnClickListener {
                        // move to Gallery App
                        pickImageFromGallery()
                        Log.d("test", "move to gallery")
                        val intent1=Intent(activity, AddSnapActivity::class.java)
                        startActivity(intent1)
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

        CoroutineScope(Dispatchers.IO).launch {
            for(i in db!!.snapDao().getAll()) {
                db.snapDao().delete(i)
            }
        }
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

    //2
    private lateinit var imageUri: Uri

    //갤러리 인텐트 실행해서 사진 선택하면, 내부저장소에 저장하라는 명령어 실행 후, 룸 데이터베이스에 snap 저장
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            // Store the image in internal storage
            storeImageInInternalStorage(uri)?.let { storedImageUri ->
                // Store the URI of the stored image in the Room database
                storeImageUriInRoomDatabase(storedImageUri)

            }
        }
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    //내부 저장소에 image.jpg라는 이름으로 저장 후 실행
    //fileName 저장할 때 마다 바꿔서 저장하게 변경하기
    private fun storeImageInInternalStorage(imageUri: Uri): Uri? {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val fileName = "image.jpg"
        val outputStream: OutputStream

        try {
            val internalStorageDir = requireContext().filesDir
            val internalStorageFile = File(internalStorageDir, fileName)
            outputStream = FileOutputStream(internalStorageFile)

            val buffer = ByteArray(4 * 1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.flush()
            outputStream.close()
            inputStream?.close()

            return Uri.fromFile(internalStorageFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    //DB에 Snap저장, 지금은 무작위 tag도 만들어서 tag도 저장
//    private fun storeImageUriInRoomDatabase(imageUri: Uri) {
//        val db = UserDatabase.getInstance(requireContext().applicationContext)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            for(i in 1..10){
//                val snap1 = Snap(i.toLong(), Date(), imageUri.toString(), "")
//                val tag1 = Tag(i.toLong(), arrayOf("t1","t2","t3","t4").random(), arrayOf(Color.GREEN,Color.RED,Color.YELLOW).random(), i.toLong())
//                db!!.snapDao().insertAll(snap1)
//                db.tagDao().insertAll(tag1)
//            }
//        }
//    }
    private fun storeImageUriInRoomDatabase(imageUri: Uri) {

        val db = UserDatabase.getInstance(requireContext().applicationContext)

        CoroutineScope(Dispatchers.IO).launch {
            val snap = Snap(1.toLong(), Date(), imageUri.toString(), "arbitrary image")
            db!!.snapDao().insertAll(snap)
        }
    }

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