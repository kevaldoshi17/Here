package com.here.models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.here.MainActivity;

import static android.content.Context.LOCATION_SERVICE;

public class MyLocationListener implements LocationListener {

    public double latestLatitude = -1.0;
    public double latestLongitude = -1.0;
    Location location;
    Context ctx;

    public MyLocationListener(Context ctx) {
        this.ctx = ctx;

        try {
            LocationManager locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(ctx, "Give Location Permission", Toast.LENGTH_SHORT).show();
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                latestLatitude = location.getLatitude();
                latestLongitude = location.getLongitude();

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                latestLatitude = location.getLatitude();
                latestLongitude = location.getLongitude();

                System.out.println("The location now is ("+latestLatitude+","+
                        latestLongitude+")");
                Toast.makeText(ctx,"The location now is ("+latestLatitude+","+latestLongitude+")",Toast.LENGTH_SHORT).show();


                MainActivity.latestLatitude = this.latestLatitude;
                MainActivity.latestLongitude = this.latestLongitude;
            }
        } catch (Exception exp) {
            Toast.makeText(ctx,"Exception "+exp, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("The location now is ("+location.getLatitude()+","+
                location.getLongitude()+")");
        Toast.makeText(ctx,"The location now is ("+location.getLatitude()+","+location.getLongitude()+")",Toast.LENGTH_SHORT).show();

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
