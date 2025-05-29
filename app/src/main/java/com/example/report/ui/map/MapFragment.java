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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.example.report.R;

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

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null; 
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_info_window, null);
                TextView title = infoView.findViewById(R.id.title);
                TextView snippet = infoView.findViewById(R.id.snippet);
                ImageView photoView = infoView.findViewById(R.id.photo);

                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());

                Object tag = marker.getTag();
                if (tag != null && tag instanceof byte[]) {
                    byte[] photoBytes = (byte[]) tag;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
                    photoView.setImageBitmap(bitmap);
                } else {
                    photoView.setImageResource(R.drawable.ic_launcher_foreground);
                }

                return infoView;
            }
        });
    }

    private void setupMap() {
        if (googleMap != null) {
            try {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

              
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
            DatabaseHelper.COLUMN_STATUS,
            DatabaseHelper.COLUMN_PHOTO
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
            byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHOTO));

            LatLng position = new LatLng(latitude, longitude);
            com.google.android.gms.maps.model.MarkerOptions markerOptions = new com.google.android.gms.maps.model.MarkerOptions()
                .position(position)
                .title(description)
                .snippet("Status: " + status);

            if ("Resolvido".equalsIgnoreCase(status)) {
                markerOptions.icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN));
            } else {
                markerOptions.icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED));
            }

            com.google.android.gms.maps.model.Marker marker = googleMap.addMarker(markerOptions);
            if (marker != null) {
                marker.setTag(photo);
            }
        }
        cursor.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
