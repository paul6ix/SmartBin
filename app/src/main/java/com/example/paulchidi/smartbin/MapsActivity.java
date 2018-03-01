package com.example.paulchidi.smartbin;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLngBounds bounds = new LatLngBounds(new LatLng(6.563810, 3.065035), new LatLng(6.674764, 3.252332));
        LatLng cu = new LatLng(6.671310, 3.158175);
        mMap.addMarker(new MarkerOptions().position(cu).title("Marker in Covenant university"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cu));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
        mMap.setLatLngBoundsForCameraTarget(bounds);
        binMarkers();

    }

    public void binMarkers() {
        LatLng maryBin = new LatLng(6.671766, 3.157018);
        LatLng ubaBin = new LatLng(6.671851, 3.155241);
        LatLng mallBin = new LatLng(6.670267, 3.157897);
        LatLng cafe1Bin = new LatLng(6.669334, 3.153195);
        mMap.addMarker(new MarkerOptions().position(maryBin).icon(BitmapDescriptorFactory.fromResource(R.drawable.sgreen_bin)).title("Mary Hall Bin"));
        mMap.addMarker(new MarkerOptions().position(ubaBin).icon(BitmapDescriptorFactory.fromResource(R.drawable.sred_bin)).title("Uba Bin"));
        mMap.addMarker(new MarkerOptions().position(mallBin).icon(BitmapDescriptorFactory.fromResource(R.drawable.syellow_bin)).title("shopping Hall Bin"));
        mMap.addMarker(new MarkerOptions().position(cafe1Bin).icon(BitmapDescriptorFactory.fromResource(R.drawable.syellow_bin)).title("cafeteria 1 Bin"));

    }
}
