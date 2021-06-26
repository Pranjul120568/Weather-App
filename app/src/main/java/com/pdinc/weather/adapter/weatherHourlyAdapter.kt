package com.pdinc.weather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdinc.weather.R
import com.pdinc.weather.models.WeatherForecast
import com.pdinc.weather.utils.convertKelvin
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class weatherHourlyAdapter:RecyclerView.Adapter<weatherHourlyAdapter.weatherHourlyViewHolder>() {
    private  var result: List<WeatherForecast?> =ArrayList()
    private lateinit var convert:convertKelvin
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): weatherHourlyViewHolder {
        return weatherHourlyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.daily_temp, parent, false)
        )
    }
    override fun onBindViewHolder(holder: weatherHourlyViewHolder, position: Int){
        holder.bind(result[position])
    }
    override fun getItemCount(): Int=result.size
    fun swapData(result:List<WeatherForecast>?){
        if (result != null) {
            this.result= result
        }
        notifyDataSetChanged()
    }
    inner class weatherHourlyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
//        private val c:Calendar= Calendar.getInstance()
//        private val sdf:SimpleDateFormat= SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
//        private val currentDate:String=sdf.format(c.time)
      fun bind(item: WeatherForecast?)=with(itemView){
          val hour:TextView=itemView.findViewById(R.id.temp_time)
          val temp:TextView=itemView.findViewById(R.id.hourlyTemp)
    val date:TextView=itemView.findViewById(R.id.temp_date)
              Log.d("fetched DAte",item!!.date)
                  hour.text = compressString(item.date)
                  date.text=item.date
                  item.weatherTemp.temp.let {
                      temp.text = (it-273.15F).toInt().toString()
                  }
      }
    }
    private fun compressString(date:String):String{
        val k=date.substring(11 ,16)
        return k
    }
    override fun getItemViewType(position: Int): Int =result.size
}