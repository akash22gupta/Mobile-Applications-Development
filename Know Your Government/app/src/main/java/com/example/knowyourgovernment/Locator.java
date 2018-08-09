package com.example.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import static android.content.Context.LOCATION_SERVICE;
import static com.example.knowyourgovernment.R.id.loc;

/**
 * Created by agupt on 4/13/2017.
 */

public class Locator {
    private static final String TAG = "Locator";
    private MainActivity owner;
    private LocationManager locationManager;
    private LocationListener locationListener;


    public Locator(MainActivity activity) {
        owner = activity;
        if (checkPermission()) {
            setUpLocationManager();
            determineLocation();
        }
    }


    public void setUpLocationManager() {

        if (locationManager != null)
            return;

        if (!checkPermission())
            return;


        locationManager = (LocationManager) owner.getSystemService(LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                // Toast.makeText(owner, "Update from " + location.getProvider(), Toast.LENGTH_SHORT).show();
                owner.doLatLon(location.getLatitude(), location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

//Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000000000, 0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER, 1000000000, 0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000000000, 0, locationListener);

    }

    public void shutDown() {
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(owner, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(owner,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }


    public boolean determineLocation() {

        if (checkPermission() == true) {
            if (locationManager != null) {
                Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    System.out.println("HEllo i am in network");
                    owner.doLatLon(loc.getLatitude(), loc.getLongitude());
                    return true;
                } else {
                    loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    if (loc != null) {
                        owner.doLatLon(loc.getLatitude(), loc.getLongitude());
                        return true;
                    } else {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            owner.doLatLon(loc.getLatitude(), loc.getLongitude());
                            return true;
                        }
                    }
                }
                return false;

            }
        }return false;
    }

}