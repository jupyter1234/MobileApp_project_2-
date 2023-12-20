package com.example.gieok_moa

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.example.gieok_moa.databinding.ItemSpinnerBinding

class TagSpinnerAdapter (
    context: Context,
    @LayoutRes private val resId: Int,
    private val values : MutableList<Tag>
):ArrayAdapter<Tag>(context, resId, values) {
    override fun getCount()=values.size

    override fun getItem(position: Int)=values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding=ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model=values[position]
        try{
            if(model.color.colorName==Color.RED.colorName){
                binding.tag.background= ContextCompat.getDrawable(binding.root.context, R.drawable.red_blur_background)
            }else if (model.color.colorName==Color.GREEN.colorName){
                binding.tag.background= ContextCompat.getDrawable(binding.root.context, R.drawable.green_blur_background)
            } else if(model.color.colorName==Color.YELLOW.colorName){
                binding.tag.background= ContextCompat.getDrawable(binding.root.context, R.drawable.yellow_blur_background)
            }
            binding.tag.text=model.staus
        } catch (e: Exception){
            Log.d("park", "error1")
            e.printStackTrace()
        }

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding=ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model=values[position]
        try{
            if(model.color.colorName==Color.RED.colorName){
                binding.tag.background= ContextCompat.getDrawable(binding.root.context, R.drawable.red_blur_background)
            }else if (model.color.colorName==Color.GREEN.colorName){
                binding.tag.background= ContextCompat.getDrawable(binding.root.context, R.drawable.green_blur_background)
            } else if(model.color.colorName==Color.YELLOW.colorName){
                binding.tag.background= ContextCompat.getDrawable(binding.root.context, R.drawable.yellow_blur_background)
            }
            binding.tag.text=model.staus
        }catch (e: Exception){
            e.printStackTrace()
        }
        return binding.root
    }


}