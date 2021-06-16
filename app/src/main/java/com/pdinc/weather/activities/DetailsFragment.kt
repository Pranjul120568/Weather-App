package com.pdinc.weather.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdinc.weather.R
import com.pdinc.weather.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
   private val binding:FragmentDetailsBinding
       get() {
           return FragmentDetailsBinding.inflate(layoutInflater)
       }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val lM=GridLayoutManager(context,1,GridLayoutManager.HORIZONTAL,false)
        binding.detailsRvHourly.layoutManager=lM

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

//    companion object {
//
//    }
}