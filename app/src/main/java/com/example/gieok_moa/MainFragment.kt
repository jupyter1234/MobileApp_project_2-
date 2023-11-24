package com.example.gieok_moa

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.room.Room
import com.example.gieok_moa.databinding.FragmentMainBinding
import com.example.gieok_moa.databinding.FragmentStat2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

        val binding = FragmentMainBinding.inflate(inflater,container,false)

        //deleteTemps()

        binding.addSnap.setOnClickListener {
            pickImageFromGallery()
        }
        // Inflate the layout for this fragment
        return binding.root
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

    //3
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

    //4
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