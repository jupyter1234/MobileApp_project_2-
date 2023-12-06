package com.example.gieok_moa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gieok_moa.databinding.FragmentStat1Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Stat1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Stat1Fragment : Fragment() {
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

    lateinit var snapViews : RecyclerView
    lateinit var datas: MutableList<Snap>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentStat1Binding.inflate(inflater,container,false)

        val db = UserDatabase.getInstance(requireContext().applicationContext)//DB선언

        val loading = CoroutineScope(Dispatchers.IO).launch {
            datas = db!!.snapDao().getAll().toMutableList()
        }
        runBlocking {
            loading.join()
        }//데이터 다 가져올 때 까지 wait
        val startOfDay = Calendar.getInstance()
        startOfDay.set(Calendar.HOUR_OF_DAY, 0)
        startOfDay.set(Calendar.MINUTE, 0)
        startOfDay.set(Calendar.SECOND, 0)
        startOfDay.set(Calendar.MILLISECOND, 0)
        val endOfDay = (startOfDay.clone() as Calendar)
        endOfDay.add(Calendar.HOUR_OF_DAY, 24)
        datas.removeIf {it.createdDate.time < startOfDay.time.time || it.createdDate.time >= endOfDay.time.time }

        val layoutManager = GridLayoutManager(activity, 3)
        binding.recyclerView.layoutManager = layoutManager

        snapViews = binding.recyclerView
        val adapter = StatAdapter(datas)
        snapViews.adapter = adapter

        binding.color1.setOnClickListener {
            val snaplist = snapsByColor(Color.RED)
            snapViews.adapter = StatAdapter(snaplist)
        }
        binding.color2.setOnClickListener {
            val snaplist = snapsByColor(Color.YELLOW)
            snapViews.adapter = StatAdapter(snaplist)
        }
        binding.color3.setOnClickListener {
            val snaplist = snapsByColor(Color.GREEN)
            snapViews.adapter = StatAdapter(snaplist)
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Stat1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Stat1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun updateDatas(){
        val db = UserDatabase.getInstance(requireContext().applicationContext)//DB선언

        val loading = CoroutineScope(Dispatchers.IO).launch {
            datas = db!!.snapDao().getAll().toMutableList()
        }
        runBlocking {
            loading.join()
        }//데이터 다 가져올 때 까지 wait
        val startOfDay = Calendar.getInstance()
        startOfDay.set(Calendar.HOUR_OF_DAY, 0)
        startOfDay.set(Calendar.MINUTE, 0)
        startOfDay.set(Calendar.SECOND, 0)
        startOfDay.set(Calendar.MILLISECOND, 0)
        val endOfDay = (startOfDay.clone() as Calendar)
        endOfDay.add(Calendar.HOUR_OF_DAY, 24)
        datas.removeIf {it.createdDate.time < startOfDay.time.time || it.createdDate.time >= endOfDay.time.time }
    }

    fun updateStat1(){
        updateDatas()

        val adapter = StatAdapter(datas)
        snapViews.adapter = adapter
    }

    fun snapsByColor(color:Color):MutableList<Snap>{
        val db = UserDatabase.getInstance(requireContext().applicationContext)//DB선언
        var datasByColor : MutableList<Snap> = mutableListOf()
        var tags : MutableList<Tag> = mutableListOf()
        val loading = CoroutineScope(Dispatchers.IO).launch {
            tags = db!!.tagDao().getAll().toMutableList()
        }
        runBlocking {
            loading.join()
        }//데이터 다 가져올 때 까지 wait
        for (i in datas){
            if(tags.any { it.ownedSnapID == i.snapId && it.color==color}){
                datasByColor.add(i)
            }
        }

        return datasByColor
    }
}