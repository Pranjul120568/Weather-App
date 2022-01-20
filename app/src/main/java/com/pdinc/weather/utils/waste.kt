package com.pdinc.weather.utils

class waste {



//        locationRequest = LocationRequest.create().apply {
//            // Sets the desired interval for active location updates. This interval is inexact. You
//            // may not receive updates at all if no location sources are available, or you may
//            // receive them less frequently than requested. You may also receive updates more
//            // frequently than requested if other applications are requesting location at a more
//            // frequent interval.
//            //
//            // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
//            // targetSdkVersion) may receive updates less frequently than this interval when the app
//            // is no longer in the foreground.
//            interval = TimeUnit.SECONDS.toMillis(60)
//
//            // Sets the fastest rate for active location updates. This interval is exact, and your
//            // application will never receive updates more frequently than this value.
//            fastestInterval = TimeUnit.SECONDS.toMillis(30)
//
//            // Sets the maximum time when batched location updates are delivered. Updates may be
//            // delivered sooner than this interval.
//            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
//
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
















//    locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//
//                // Normally, you want to save a new location to a database. We are simplifying
//                // things a bit and just saving it as a local variable, as we only need it again
//                // if a Notification is created (when the user navigates away from app).
//                currentLocation = locationResult.lastLocation
//                // Notify our Activity that a new location was added. Again, if this was a
//                // production app, the Activity would be listening for changes to a database
//                // with new locations, but we are simplifying things a bit to focus on just
//                // learning the location side of things.
////                val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
////                intent.putExtra(EXTRA_LOCATION, currentLocation)
////                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
//             // Updates notification content if this service is running as a foreground
//                // service.
//
//            }
//        }
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
}