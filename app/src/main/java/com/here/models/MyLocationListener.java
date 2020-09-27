package com.here.models;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.here.MainActivity;

public class MyLocationListener implements LocationListener {

    public double latestLatitude = -1.0;
    public double latestLongitude = -1.0;
    Context ctx;
    LocationManager locationManager;

    public MyLocationListener(Context ctx, LocationManager locationManager) {
        this.ctx = ctx;
        this.locationManager = locationManager;
    }

    @Override
    public void onLocationChanged(Location location) {
        latestLatitude = location.getLatitude();
        latestLongitude = location.getLongitude();

        MainActivity.latestLatitude = this.latestLatitude;
        MainActivity.latestLongitude = this.latestLongitude;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
