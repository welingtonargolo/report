package com.example.report.ui.map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.report.data.DatabaseHelper;
import com.example.report.databinding.FragmentMapBinding;
import com.example.report.utils.LocationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private GoogleMap googleMap;
    private LocationHelper locationHelper;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize helpers
        locationHelper = new LocationHelper(requireContext());
        dbHelper = new DatabaseHelper(requireContext());

        // Setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(com.example.report.R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Setup click listeners
        binding.fabMyLocation.setOnClickListener(v -> moveToCurrentLocation());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        setupMap();
        loadProblems();
    }

    private void setupMap() {
        if (googleMap != null) {
            try {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                // Move to current location when map is ready
                moveToCurrentLocation();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveToCurrentLocation() {
        locationHelper.getCurrentLocation(new LocationHelper.OnLocationUpdateListener() {
            @Override
            public void onLocationUpdate(android.location.Location location) {
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
            }

            @Override
            public void onLocationError(String error) {
                // Handle error
            }
        });
    }

    private void loadProblems() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_DESCRIPTION,
            DatabaseHelper.COLUMN_LATITUDE,
            DatabaseHelper.COLUMN_LONGITUDE,
            DatabaseHelper.COLUMN_STATUS
        };

        Cursor cursor = db.query(
            DatabaseHelper.TABLE_PROBLEMS,
            projection,
            null,
            null,
            null,
            null,
            null
        );

        while (cursor.moveToNext()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LONGITUDE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS));

            LatLng position = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title(description)
                .snippet("Status: " + status));
        }
        cursor.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
