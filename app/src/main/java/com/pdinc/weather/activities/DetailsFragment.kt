package com.pdinc.weather.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pdinc.weather.R
import com.pdinc.weather.adapter.weatherHourlyAdapter
import com.pdinc.weather.databinding.FragmentDetailsBinding
import com.pdinc.weather.models.FetchAll
import com.pdinc.weather.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.runBlocking


class DetailsFragment : Fragment() {
    private val searchName = WeatherRemoteDataSourceImpl()
    private lateinit var binding:FragmentDetailsBinding
    private  val hourlyAdapter= weatherHourlyAdapter()
    private val LOCATION_REQUEST_CODE = 2
    val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    )
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var request: LocationRequest
    private lateinit var builder: LocationSettingsRequest.Builder
    private lateinit var locationCallback: LocationCallback
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        checkGpsPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return inflater.inflate(R.layout.fragment_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentDetailsBinding.bind(view)
        checkGpsPermission()
        Log.d("TAG", "has permisions")
        startGPS()
        val lM=GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        binding.detailsRvHourly.apply {
            layoutManager = lM
            binding.detailsRvHourly.adapter = hourlyAdapter
        }
        binding.detailsRvDaily.layoutManager=lM
        binding.detailsRvDaily.adapter
    }


    private suspend fun searchByGPS(location: Location) {
        val r=searchName.getWeatherByGps(location.latitude, location.longitude)
        if(r.body()!=null){
            hourlyAdapter.swapData(r.body())

        }else{
            Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show()
        }
    }

}