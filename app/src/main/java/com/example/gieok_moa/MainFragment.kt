package com.example.gieok_moa

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import android.os.Build.VERSION_CODES.S
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

//lateinit var imageUri: Uri?
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val REQUEST_IMAGE_CAPTURE = 1

    private val PICK_IMAGE_REQUEST = 1  // 갤러리에서 이미지를 선택하는 요청 코드
    private lateinit var binding: FragmentMainBinding // 바인딩 클래스 인스턴스

    private lateinit var datas: MutableList<Snap>
    lateinit var myadapter : MyAdapter
    lateinit var snapAddImageUrl : String

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

        snapAddImageUrl = "android.resource://com.example.gieok_moa/drawable/snap_add_button1"
        val snapAddButton: Snap = Snap(0.toLong(), Date(), snapAddImageUrl, "")
        datas.add(snapAddButton)

        val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager

        myadapter = MyAdapter(datas){ snap ->
            //Log.d("check", "snap clicked")
            if (snap.photoUrl == snapAddImageUrl){
                // when snap add button clicked -> popup & move to snap add page
                // selection dialog
                val selectionDialog = SelectionDialogBinding.inflate(layoutInflater)
                AlertDialog.Builder(requireActivity()).run {
                    setView(selectionDialog.root)
                    selectionDialog.takePic.setOnClickListener {
                        // move to Camera App
                        openCamera()
                    }
                    selectionDialog.fromGal.setOnClickListener {
                        // move to Gallery App
                        openGallery()
                    }
                    show()
                }
            }
            else{
                // when snap clicked -> dialog
                val snapdialogBinding = SnapLayoutBinding.inflate(layoutInflater)

                val dialog = Dialog(requireContext())
                dialog.setContentView(snapdialogBinding.root)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                // Dialog의 크기를 조절합니다. 여기서는 폭과 높이를 설정합니다.
                val width = resources.getDimensionPixelSize(R.dimen.dialog_width) // 원하는 폭의 크기를 설정하세요
                val height = resources.getDimensionPixelSize(R.dimen.dialog_height) // 원하는 높이의 크기를 설정하세요
                dialog.window?.setLayout(width, height)

                for (i in datas) {
                    if (i.snapId == snap.snapId){
                        Log.d("ko", "${snap.photoUrl.toUri()}")
                        Glide.with(snapdialogBinding.root)
                            .load(snap.photoUrl.toUri())
                            .error(R.drawable.cancel)
                            .into(snapdialogBinding.snapimage)
                        snapdialogBinding.snapText.text = snap.comment
                    }
                }

                val trash=snapdialogBinding.root.findViewById<ImageView>(R.id.trashbutton)
                trash.setOnClickListener {
                    //삭제
                    Log.d("ko", "remove button")
                    db!!.snapDao().delete(snap)
                    datas.remove(snap)
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        binding.recyclerView.adapter = myadapter

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

//    override fun onStart() {
//        super.onStart()
//        val db = UserDatabase.getInstance(requireContext().applicationContext)//DB선언
//
//        val loading = CoroutineScope(Dispatchers.IO).launch {
//            datas = db!!.snapDao().getAll().toMutableList()
//        }
//        runBlocking {
//            loading.join()
//        }//데이터 다 가져올 때 까지 wait
//
//        binding.recyclerView.adapter = myadapter
//    }

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

            // add to datas
            val db = UserDatabase.getInstance(requireContext().applicationContext)
            CoroutineScope(Dispatchers.IO).launch {
                for (snap in db!!.snapDao().getAll()){
                    if (snap.photoUrl == uri.toString()){
                        datas.add(datas.size-1, snap)
                        }
                    }
                }
            }
        }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    //2
    // 카메라 관련 코드
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.d("ko", "f1")
        requestCameraLauncher.launch(intent)
        Log.d("ko", "f2")
    }

    private val requestCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap: Bitmap = data?.extras?.get("data") as Bitmap
            val uriImage = bitmapToUriConverter(imageBitmap)
            imageUri = uriImage

            val intent1 = Intent(activity, AddSnapActivity::class.java)
            startActivity(intent1)

            Log.d("ko", "dddd")

            // add to datas
            val db = UserDatabase.getInstance(requireContext().applicationContext)
            CoroutineScope(Dispatchers.IO).launch {
                for (snap in db!!.snapDao().getAll()) {
                    if (snap.photoUrl == uriImage.toString()) {
                        datas.add(datas.size - 1, snap)
                    }
                }
            }
        }
        // Now you have the image bitmap, you can use it as needed (e.g., save to storage, display in ImageView, etc.)
        else {
            // The user canceled or encountered an error
            Log.d("ko", "failed to load image")
        }
    }
    private fun bitmapToUriConverter(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(requireContext().applicationContext?.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

}

interface OnSnapAddedListener {
    fun onSnapAdded()
}