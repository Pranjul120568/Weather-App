package com.pdinc.weather.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdinc.weather.R
import com.pdinc.weather.models.WeatherForecast
import com.pdinc.weather.utils.CheckMonth

class weatherHourlyAdapter:RecyclerView.Adapter<weatherHourlyAdapter.weatherHourlyViewHolder>() {
    private  var result: List<WeatherForecast?> =ArrayList()
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
      @SuppressLint("SetTextI18n")
      fun bind(item: WeatherForecast?)=with(itemView){
          val hour:TextView=itemView.findViewById(R.id.temp_time)
          val temp:TextView=itemView.findViewById(R.id.hourlyTemp)
    val date:TextView=itemView.findViewById(R.id.temp_date)
          Log.d("DATE IS ",item!!.date)
                  hour.text = compressString(item!!.date)
                  date.text=compressMonth(item.date)
                  item.weatherTemp.temp.let {
                      temp.text = (it-273.15F).toInt().toString()+"°C"
                  }
      }
    }
    private fun compressString(date:String):String{
        var timeToBeReturned=""
        val timeToBeConverted=date.substring(11,13)
        var time=timeToBeConverted.toInt()
        if(time>=12){
            time=24-time
            timeToBeReturned=time.toString()+" "+"PM"
        }else if(time==0){
            timeToBeReturned="12"+" "+"AM"
        }
        else{
            timeToBeReturned=time.toString()+" "+"AM"
        }
        return timeToBeReturned
    }
    private fun compressMonth(date:String):String{
        val k=date.substring(8,10)
        val k1=date.substring(5,7)
        val monthList=CheckMonth().addMonths()
        val month=monthList[k1.toInt()]
        return k+" "+month
    }
    override fun getItemViewType(position: Int): Int =result.size
}
