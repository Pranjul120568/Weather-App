package com.pdinc.weather.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pdinc.weather.R
import com.pdinc.weather.adapter.weatherHourlyAdapter
import com.pdinc.weather.databinding.FragmentDetailsBinding
import com.pdinc.weather.models.WeatherForecast
import com.pdinc.weather.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


class DetailsFragment : Fragment() {
    private val searchName = WeatherRemoteDataSourceImpl()
    private lateinit var binding:FragmentDetailsBinding
    private  val hourlyAdapter= weatherHourlyAdapter()
    private val LOCATION_REQUEST_CODE = 2
    val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    )
    private var currentLocation:Location?=null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private  var originalList= arrayListOf<WeatherForecast?>()
    private lateinit var locationCallback: LocationCallback
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentDetailsBinding.bind(view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkGpsPermission()
        binding.detailsRvHourly.isPaddingRelative
        hourlyAdapter
        Log.d("TAG", "has permisions")
        val lM=GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        binding.detailsRvHourly.apply {

            layoutManager = lM
            binding.detailsRvHourly.adapter = hourlyAdapter
        }
//        binding.detailsRvDaily.layoutManager=lM
//        binding.detailsRvDaily.adapter
    }

    private fun startGPS() {
        locationRequest = LocationRequest.create().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            //
            // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
            // targetSdkVersion) may receive updates less frequently than this interval when the app
            // is no longer in the foreground.
            interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    Log.d("Location",location!!.latitude.toString()+" "+location.longitude.toString())
                    if (location != null) {
                        runBlocking{ searchByGPS(location) }
                    }
                }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                // Normally, you want to save a new location to a database. We are simplifying
                // things a bit and just saving it as a local variable, as we only need it again
                // if a Notification is created (when the user navigates away from app).
                currentLocation = locationResult.lastLocation
                // Notify our Activity that a new location was added. Again, if this was a
                // production app, the Activity would be listening for changes to a database
                // with new locations, but we are simplifying things a bit to focus on just
                // learning the location side of things.
                val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                intent.putExtra(EXTRA_LOCATION, currentLocation)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
             // Updates notification content if this service is running as a foreground
                // service.

            }
        }
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())


    }

    private suspend fun searchByGPS(location: Location) {
        val r=searchName.getWeatherByGps(location.latitude.toInt(), location.longitude.toInt())
        if(r.body()!=null){
            originalList.addAll(r.body()!!.fetchlist)
            hourlyAdapter.swapData(r.body()!!.fetchlist)
        }else{
            Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show()
        }
    }
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
    companion object {
        private const val TAG = "ForegroundOnlyLocationService"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
                "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.location"

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
                "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"

        private const val NOTIFICATION_ID = 12345678

        private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"
    }
}