package com.pdinc.weather.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdinc.weather.R
import com.pdinc.weather.models.WeatherForecast
import java.text.SimpleDateFormat

class weatherDailyAdapter: RecyclerView.Adapter<weatherDailyAdapter.DailyViewHolder>() {
    private var result:List<WeatherForecast?> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.daily_temp,parent,false)
        )
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        result.let {
            holder.bind(it)
        }
    }
    fun swapData(result:List<WeatherForecast>?){
        if (result != null) {
            this.result= result
        }
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int =result.size
    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: List<WeatherForecast?>)=with(itemView){
            val hour: TextView =itemView.findViewById(R.id.temp_time)
            val temp: TextView =itemView.findViewById(R.id.hourlyTemp)
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun getDateFromTimeStamp(dt: Long): String? {
        val date = dt
        return SimpleDateFormat("hh:mm").format(date)
    }

}