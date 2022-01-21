package com.pdinc.weather.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.pdinc.weather.R
import com.pdinc.weather.adapter.searchCityAdapter
import com.pdinc.weather.adapter.weatherHourlyAdapter
import com.pdinc.weather.databinding.FragmentSearchBinding
import com.pdinc.weather.models.CurrentWeather
import com.pdinc.weather.models.FetchAll
import com.pdinc.weather.models.WeatherForecast
import com.pdinc.weather.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.*
import retrofit2.Response

class SearchFragment : Fragment() {
    private val searchName = WeatherRemoteDataSourceImpl()
    private lateinit var searchItem1:SearchView
    private lateinit var binding:FragmentSearchBinding
    private  val hourlyAdapter= weatherHourlyAdapter()
    private  var originalList= arrayListOf<WeatherForecast?>()
    private val search_adapter=searchCityAdapter()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val k= inflater.inflate(R.layout.fragment_search, container, false)
        return k
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentSearchBinding.bind(view)
        searchItem1=binding.searchPlace
        searchItem1.onActionViewExpanded()
        searchItem1.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.trim().isNotEmpty()) {
                    runBlocking { searchByName(query) }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                }
                return true
            }
        })
        val hourlyLayoutManager= GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        binding.detailsRvHourly1.apply {
            layoutManager = hourlyLayoutManager
            this.adapter = hourlyAdapter
        }
    }

    private fun turnOnVisibility() {
      binding.rl1.visibility=View.VISIBLE

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)
            inflater.inflate(R.menu.navigate, menu)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        private suspend fun searchByName(query: String): Response<CurrentWeather> {
            val re:Response<CurrentWeather>
            withContext(Dispatchers.IO) {
               re= searchName.getCurrentWeatherBySearch(query)
            }
            if(re.body()!=null) {
                Log.d("Data Fetched","Presented")
                searchListByName(query)
                setData(re)
                turnOnVisibility()
            }else{
                Log.d("Data did not fetch","Not presented")
                Toast.makeText(requireContext(),"Please enter valid city name!",Toast.LENGTH_SHORT).show()
            }
            return re
        }
    private suspend fun searchListByName(query: String):Response<FetchAll>{
        val re:Response<FetchAll>
        withContext(Dispatchers.IO) {
            re=searchName.getWeatherBySearch(query)
        }
        if(re.body()!=null){
            Log.d("Data Fetched","Presented")
            inflateRv(re)
        }else{
            Log.d("Data did not fetch","Not presented")
            Toast.makeText(requireContext(),"Please enter valid city name!",Toast.LENGTH_SHORT).show()
        }
        return re
    }

    private fun inflateRv(re: Response<FetchAll>) {
        originalList.addAll(re.body()!!.fetchlist)
        hourlyAdapter.swapData(re.body()!!.fetchlist)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(it: Response<CurrentWeather>) {
            GlobalScope.launch(Dispatchers.Main){
                it.body().apply {
                    binding.cloudsTv1.text=this!!.clouds!!.all.toString()+"%"
                    binding.placeTv1.text = this!!.name
                    var temp= main?.temp?.minus(273.15)!!.toInt()
//                          temp= String.format("%.1f",temp).toInt()
                    val tempstr=temp.toString()+"°C"
                    binding.tempraturemainScreenTv1.text = tempstr
                    binding.humidityTv1.text = this.main.humidity.toString()
                    binding.pressureTv1.text = this.main.pressure.toString()
                    binding.windTv1.text = this.wind!!.speed.toString()
                    val sunset = this.sys!!.sunset
                    val sunrise = this.sys.sunrise
                    val id= this.weather!![0]!!.id
                    Log.d("TAG",id.toString())
                    //changeColor(sunset!!, sunrise!!,id!!)
                    var weatherdescription=weather[0]!!.description
                    var c=weatherdescription!![0].toUpperCase()
                    var descript=c+weatherdescription.substring(1)
                    binding.descriptiontv1.text=descript
                    changeColor(sunrise!!,sunset!!,this.weather[0]!!.id!!)
                }
            }
        }
    private fun convertTemp(temprature:Double):String{
        var temp=temprature
        temp-=273.15
        val returntemp=temp.toInt().toString()
        return returntemp
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun changeColor(sunrise:Int, sunset :Int,main:Int){
        val placeTv=requireView().findViewById<TextView>(R.id.placeTv1)
        val tempraturemainScreenTv=requireView().findViewById<TextView>(R.id.tempraturemainScreenTv1)
        val descriptiontv=requireView().findViewById<TextView>(R.id.descriptiontv1)
        val time=System.currentTimeMillis()
        val suns=sunset.toLong()
        val sunr=sunrise.toLong()
        Log.d("TIME IS",time.toString()+" "+(suns*1000).toString()+" "+(sunr*1000).toString())
        if(time<sunr*1000 && time>suns*1000){
            binding.rl1.setBackgroundResource(R.color.white)
            Log.d("",main.toString())
            if(main==800){
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.iconssun).into(binding.placeIv1)
                    binding.placeIv1.setBackgroundResource(R.color.white)
                }
            }else if(main>800){
                binding.placeIv1.setImageResource(R.drawable.iconsclouds)
            }else if(main>=300 && main<=500){
                GlobalScope.launch(Dispatchers.Main){
                    Glide.with(requireContext()).load(R.drawable.icons8raincloud).into(binding.placeIv1)
                }
            }else{
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.icons8rain).into(binding.placeIv1)
                }
            }
        }else {
            Log.d("TAG",main.toString())
            binding.rl1.setBackgroundResource(R.color.night_dark)
            binding.placeIv1.setBackgroundResource(R.color.night_dark)
            placeTv.setTextColor(resources.getColor(R.color.white))
            tempraturemainScreenTv.setTextColor(resources.getColor(R.color.white))
            descriptiontv.setTextColor(resources.getColor(R.color.white))
            Log.d("HERE IS THE COLOR",R.color.white.toString())
            if(main==800){
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.iconsfog).into(binding.placeIv1)
                }
            }else if(main>800){
                binding.placeIv1.setImageResource(R.drawable.iconsclouds)
            }else{
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.icons8rain).into(binding.placeIv1)
                }
            }
        }
    }
    }