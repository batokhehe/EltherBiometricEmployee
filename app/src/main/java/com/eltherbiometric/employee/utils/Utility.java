package com.eltherbiometric.employee.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Utility {

    private Activity mActivity;
    private Context mContext;
    private FusedLocationProviderClient mFusedLocationClient;
    public String[] Location = {""};

    public Utility(Context context, Activity activity) {
        mActivity = activity;
        mContext = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public String[] GetLocation(){
        // GET CURRENT LOCATION
//        mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null){
//                    // Do it all with location
//                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
//                    // Display in Toast
//                    Toast.makeText(context,
//                            "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(),
//                            Toast.LENGTH_LONG).show();
//                    Location[0] = String.valueOf(location.getLatitude());
//                    Location[1] = String.valueOf(location.getLongitude());
//                }
//            }
//        });
//        return Location;

        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            LocationRequest locationRequest;
                            LocationCallback locationCallback;
                            locationRequest = LocationRequest.create();
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            locationRequest.setInterval(20 * 1000);
                            locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    if (locationResult == null) {
                                        return;
                                    }
                                    for (Location location : locationResult.getLocations()) {
                                        if (location != null) {
                                            Location[0] = String.valueOf(location.getLatitude());
                                            Location[1] = String.valueOf(location.getLongitude());
                                        }
                                    }
                                }
                            };
                        } else {
                            Log.d("Utility Location", "onComplete: " + location.getLatitude()+ " , " + location.getLongitude());
//                            latTextView.setText(location.getLatitude()+"");
//                            lonTextView.setText(location.getLongitude()+"");
                            Location[0] = String.valueOf(location.getLatitude());
                            Location[1] = String.valueOf(location.getLongitude());
                        }
                    }
                }
        );

        return Location;
    }

//    @SuppressLint("MissingPermission")
//    private void requestNewLocationData(){
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setNumUpdates(1);
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
//        mFusedLocationClient.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//        );
//
//    }

}
