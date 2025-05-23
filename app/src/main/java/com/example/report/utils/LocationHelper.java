package com.example.report.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationHelper {
    private static final long UPDATE_INTERVAL = 10000; // 10 seconds
    private static final long FASTEST_INTERVAL = 5000; // 5 seconds

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private OnLocationUpdateListener listener;

    public interface OnLocationUpdateListener {
        void onLocationUpdate(Location location);
        void onLocationError(String error);
    }

    public LocationHelper(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        // Create location request
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
            .setIntervalMillis(UPDATE_INTERVAL)
            .setMinUpdateIntervalMillis(FASTEST_INTERVAL)
            .build();

        // Create location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (listener != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        listener.onLocationUpdate(location);
                    }
                }
            }
        };
    }

    public void setLocationUpdateListener(OnLocationUpdateListener listener) {
        this.listener = listener;
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onLocationError("Location permissions not granted");
            }
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void getCurrentLocation(OnLocationUpdateListener callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onLocationError("Location permissions not granted");
            return;
        }

        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(location -> {
                if (location != null) {
                    callback.onLocationUpdate(location);
                } else {
                    callback.onLocationError("Could not get current location");
                }
            })
            .addOnFailureListener(e -> callback.onLocationError(e.getMessage()));
    }
}
