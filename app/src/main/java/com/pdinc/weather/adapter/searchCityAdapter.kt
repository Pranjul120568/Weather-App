package com.pdinc.weather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pdinc.weather.R
import com.pdinc.weather.databinding.PlaceItemBinding
import com.pdinc.weather.models.CurrentWeather

class searchCityAdapter() :RecyclerView.Adapter<searchCityAdapter.SearchCityViewHolder>() {
    private var result:List<CurrentWeather> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityViewHolder {
        val bindingtoinflate=PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchCityViewHolder(bindingtoinflate)
    }

    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) = holder.bind(result[position])
    fun swapData(data:List<CurrentWeather>){
        this.result=data
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int =result.size
    inner class SearchCityViewHolder(private val binding: PlaceItemBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(item:CurrentWeather)=with(binding){
            binding.placeNameTv.text=item.name
            var tempra=convertTemp(item.main!!.temp!!)+"°C"
            binding.tempTv.text= tempra
        }
    }
    fun convertTemp(temprature:Double):String{
        var temp=temprature
        temp-=273.15
        val returntemp=temp.toInt().toString()
        return returntemp
    }
}
