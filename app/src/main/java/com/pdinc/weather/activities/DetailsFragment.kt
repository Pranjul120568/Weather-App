package com.pdinc.weather.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pdinc.weather.R
import com.pdinc.weather.adapter.weatherHourlyAdapter
import com.pdinc.weather.databinding.FragmentDetailsBinding
import com.pdinc.weather.models.WeatherForecast
import com.pdinc.weather.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class DetailsFragment : Fragment() {
    private val searchName = WeatherRemoteDataSourceImpl()
    private lateinit var binding:FragmentDetailsBinding
    private  val hourlyAdapter= weatherHourlyAdapter()
    private val LOCATION_REQUEST_CODE = 2
    private lateinit var rl:RelativeLayout
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private var currentLocation:Location?=null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var c=Calendar.getInstance()
    private  var originalList= arrayListOf<WeatherForecast?>()
    private lateinit var locationCallback: LocationCallback
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentDetailsBinding.bind(view)
        rl=view.findViewById(R.id.detailsRl)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkGpsPermission()
        binding.detailsRl.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.sun_yellow))
        Log.d("TAG", "has permisions")
        val hourlyLayoutManager=GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        binding.detailsRvHourly.apply {
            layoutManager = hourlyLayoutManager
            binding.detailsRvHourly.adapter = hourlyAdapter
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun startGPS() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                Log.d("Location",location!!.latitude.toString()+" "+location.longitude.toString())
                runBlocking{ searchForecastByGPS(location)
                    getCurrentByGps(location)
                }
            }
    }

    private suspend fun searchForecastByGPS(location: Location) {
        val r=searchName.getWeatherByGps(location.latitude, location.longitude)
        if(r.body()!=null){
            originalList.addAll(r.body()!!.fetchlist)
            hourlyAdapter.swapData(r.body()!!.fetchlist)
        }else{
            Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getCurrentByGps(location: Location){
        val r=searchName.getCurrentWeatherByGps(location.latitude,location.longitude)
        if(r.body()!=null){
            GlobalScope.launch (Dispatchers.IO){
                r.body().apply {
                    binding.placeTv.text = this!!.name
                    var temp= main?.temp?.minus(273.15)!!.toInt()
//                          temp= String.format("%.1f",temp).toInt()
                    val tempstr=temp.toString()+"°C"
                    binding.tempraturemainScreenTv.text = tempstr
                    binding.humidityTv.text = this.main.humidity.toString()
                    binding.pressureTv.text = this.main.pressure.toString()
                    binding.windTv.text = this.wind!!.speed.toString()
                    val sunset = this.sys!!.sunset
                    val sunrise = this.sys.sunrise
                    val id=this.weather!!.get(0)!!.id
                    Log.d("TAG",id.toString())
                    changeColor(sunset!!, sunrise!!,id!!)
                    var weatherdescription=weather[0]!!.description
                    var c=weatherdescription!![0].toUpperCase()
                    var descript=c+weatherdescription.substring(1)
                    binding.descriptiontv.text=descript
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkGpsPermission() {
        when {
            hasPermissions(requireContext(), *permissions) -> startGPS()
            shouldShowRequestPermissionRationale(permissions[0]) -> permissionExplanation()
            shouldShowRequestPermissionRationale(permissions[1]) -> permissionExplanation()
            else -> requestPermissions(
                permissions,
                LOCATION_REQUEST_CODE
            )
        }
    }
    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startGPS()
            } else {
                val showRationale = shouldShowRequestPermissionRationale(permissions[0])
                if (!showRationale) {
                    //Never ask again
                    permissionExplanation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
     private fun openPermissionSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 0)
    }

    private fun permissionExplanation() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MaterialComponents_Dialog)
        builder.setTitle(getString(R.string.location_required))
        builder.setMessage(getString(R.string.access_to_gps))
        builder.apply {
            setPositiveButton(
                R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
                if (shouldShowRequestPermissionRationale(permissions[0]) || shouldShowRequestPermissionRationale(
                        permissions[1]
                    )
                )
                    requestPermissions(
                        permissions,
                        LOCATION_REQUEST_CODE
                    )
                else
                    openPermissionSetting()
            }
            setNegativeButton(
                R.string.cancel
            ) { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder.create()
        builder.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun changeColor(sunrise:Int, sunset :Int,main:Int){
        val placeTv=requireView().findViewById<TextView>(R.id.placeTv)
        val tempraturemainScreenTv=requireView().findViewById<TextView>(R.id.tempraturemainScreenTv)
        val descriptiontv=requireView().findViewById<TextView>(R.id.descriptiontv)

        val time=System.currentTimeMillis()
        val suns=sunset.toLong()
        val sunr=sunrise.toLong()
        Log.d("TIME IS",time.toString()+" "+(suns*1000).toString()+" "+(sunr*1000).toString())
        if(time<sunr*1000 && time>suns*1000){
            rl.setBackgroundResource(R.color.white)
            Log.d("",main.toString())
            if(main==800){
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.iconssun).into(binding.placeIv)
                    binding.placeIv.setBackgroundResource(R.color.white)
                }
            }else if(main>800){
                binding.placeIv.setImageResource(R.drawable.iconsclouds)
            }else if(main>=300 && main<=500){
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.icons8raincloud).into(binding.placeIv)
                }
            }else{
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.icons8rain).into(binding.placeIv)
                }
            }
        }else{
            Log.d("TAG",main.toString())
            rl.setBackgroundResource(R.color.night_dark)
            binding.placeIv.setBackgroundResource(R.color.night_dark)
            placeTv.setTextColor(resources.getColor(R.color.white))
            tempraturemainScreenTv.setTextColor(resources.getColor(R.color.white))
            descriptiontv.setTextColor(resources.getColor(R.color.white))
            Log.d("HERE IS THE COLOR",R.color.white.toString())
            if(main==800){
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.iconsfog).into(binding.placeIv)
                }
            }else if(main>800){
                binding.placeIv.setImageResource(R.drawable.iconsclouds)
            }else{
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext()).load(R.drawable.icons8rain).into(binding.placeIv)
                }
            }
        }
    }
    //api.openweathermap.org/data/2.5/weather?lat=28.6810615&lon=77.3394563&appid=c053cb48689e689aa1784cc74e6e4f6b
}