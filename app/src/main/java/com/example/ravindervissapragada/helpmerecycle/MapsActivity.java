package com.example.ravindervissapragada.helpmerecycle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager lm;
    private int locationRequestCode = 1000;
    private double lat, lon;
    private boolean hasLatLon = false;
    private boolean mapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Ask for permission if not already granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, locationRequestCode);
        } else {
            addLocationListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addLocationListener();
                }
                break;
        }
    }

    protected void addLocationListener() {
        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location l) {
                if (l != null) {
                    lat = l.getLatitude();
                    lon = l.getLongitude();
                    hasLatLon = true;
                    putMarker();
                }
            }

            @Override
            public void onProviderDisabled(String s) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onStatusChanged(String s, int i, Bundle b) {}
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, ll, Looper.myLooper());
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1, ll, Looper.myLooper());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;
        putMarker();
    }

    protected void putMarker() {
        if (!mapReady || !hasLatLon) return;
        // Create LatLon Object and put it on the map.
        LatLng latlon = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions()
                .position(latlon)
                .title("You Are Here"));
    }
}
