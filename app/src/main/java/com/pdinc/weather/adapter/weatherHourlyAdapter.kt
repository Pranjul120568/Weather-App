package com.pdinc.weather.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdinc.weather.R
import com.pdinc.weather.models.FetchAll
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class weatherHourlyAdapter():RecyclerView.Adapter<weatherHourlyAdapter.weatherHourlyViewHolder>() {
    private var result:List<FetchAll?>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): weatherHourlyViewHolder {
        return weatherHourlyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.daily_temp, parent, false)
        )
    }
    override fun onBindViewHolder(holder: weatherHourlyViewHolder, position: Int) {
        result?.get(position).let {
            if (it != null) {
                holder.bind(it)
            }
        }
    }
    override fun getItemCount(): Int= result!!.size
    inner class weatherHourlyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val c:Calendar= Calendar.getInstance()
        val sdf:SimpleDateFormat= SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
        val currentDate:String=sdf.format(c.time)
      fun bind(item: FetchAll){
          val hour:TextView=itemView.findViewById(R.id.temp_time)
          val temp:TextView=itemView.findViewById(R.id.hourlyTemp)
          if(item.fetchlist.get(position).date==currentDate){
              hour.text = getDateFromTimeStamp(item.fetchlist.get(position).dateunrefined).toString()
              item.fetchlist.get(position).weatherTemp.get(position).let {
                  temp.text=it.temp.toString()
              }
          }
      }
    }
    fun swapData(result:List<FetchAll?>?){
        this.result=result
        notifyDataSetChanged()
    }


    @SuppressLint("SimpleDateFormat")
    private fun getDateFromTimeStamp(dt: Long): String? {
        val date = dt
        return SimpleDateFormat("hh:mm").format(date)
    }
}