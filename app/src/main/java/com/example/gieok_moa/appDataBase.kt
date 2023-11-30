package com.example.gieok_moa

import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Date

@Database(entities = arrayOf(User::class, Snap::class, Tag::class), version = 1)
@TypeConverters(
    value = [
        Converters::class,
    ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): userDao
    abstract fun snapDao(): snapDao
    abstract fun tagDao(): tagDao
    companion object {
        private var instance: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase? {
            if (instance == null) {
                synchronized(UserDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user-database"
                    ).build()
                }
            }
            return instance
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

/*

class DBExplain{
    //db선언
    val db = UserDatabase.getInstance(this.applicationContext)//선언


    //데이터 불러오기
    lateinit var datas: List<Snap>

    val loading = CoroutineScope(Dispatchers.IO).launch {
        datas = db!!.snapDao().getAll()
    }

    runBlocking {
        loading.join()
    }//coroutine이 끝날 때 까지 wait, 필요한 경우에만 쓰세요

    //데이터 삽입하기
    CoroutineScope(Dispatchers.IO).launch {
        val i = 0
        val imageUri = "".toUri()
        val snap1 = Snap(i.toLong(), Date(), imageUri.toString(), "")
        db!!.snapDao().insertAll(snap1)
    }
}
 */

/*
class ImageLoadingExplain {
    //이하는 사진 가져오고 표시하는 코드, GPT가 짜준건데 참고하세요
    //pickImageFromGallery : 갤러리에서 사진 가져오기
    //storeImageInInternalStorage : 내부저장소에 사진 저장
    //storeImageUriInRoomDatabase : Snap에 이미지를 넣어서 snap을 DB에 저장

    //requireContext() <- 이거 보이면 this로 바꿔서 쓰세요

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
            val snap1 = Snap(i.toLong(), Date(), imageUri.toString(), "")
            db!!.snapDao().insertAll(snap1)
            }

        }
    }

    //5 이미지 표시하기
    1. Glide 라이브러리 추가

    2. 다음 코드로 표시
    Glide.with(binding.root)
            .load(snap.photoUrl.toUri())
            .into(binding.imageView) <- id
}


 */