package com.example.paulchidi.smartbin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //All declaration for the map
    private GoogleMap mMap;
    private static final String apiUrl = "https://smart-bin-app.herokuapp.com/";
    private static final String consoleLog = " Error Data";
    // Bin coordinates -----------------------------------------------
    LatLng maryBin = new LatLng(6.671766, 3.157018);
    LatLng ubaBin = new LatLng(6.671851, 3.155241);
    LatLng mallBin = new LatLng(6.670267, 3.157897);
    LatLng cafe1Bin = new LatLng(6.669334, 3.153195);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLngBounds bounds = new LatLngBounds(new LatLng(6.563810, 3.065035), new LatLng(6.674764, 3.252332));
        LatLng cu = new LatLng(6.671310, 3.158175);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cu));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
        mMap.setLatLngBoundsForCameraTarget(bounds);
        binMarkers();

    }

    public void binMarkers() {
setupMap();

        MarkerOptions mapOptions = new MarkerOptions();
        String status = "medium";

        if (status == "high") {
            mapOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.sred_bin));

        } else if (status == "medium") {
            mapOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.syellow_bin));
        }
        mMap.addMarker(mapOptions.title("Map option testing").position(maryBin));
        mMap.addMarker(new MarkerOptions().position(ubaBin).icon(BitmapDescriptorFactory.fromResource(R.drawable.sred_bin)).title("Uba Bin"));
        mMap.addMarker(new MarkerOptions().position(mallBin).icon(BitmapDescriptorFactory.fromResource(R.drawable.syellow_bin)).title("shopping Hall Bin"));
        mMap.addMarker(new MarkerOptions().position(cafe1Bin).icon(BitmapDescriptorFactory.fromResource(R.drawable.sred_bin)).title("cafeteria 1 Bin"));
      /*  mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                onCreateDialog();

                return true;
            }
        });*/
    }


    public void onCreateDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_title)
                .setItems(R.array.alert_otpions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Toast.makeText(getApplicationContext(), R.string.alert_truck, Toast.LENGTH_LONG).show();

                        } else if (which == 1) {
                            Toast.makeText(getApplicationContext(), "Plotting Route", Toast.LENGTH_LONG).show();
                            Polyline line = mMap.addPolyline(new PolylineOptions().add(ubaBin, cafe1Bin).width(5).color(Color.BLUE));
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void setupMap(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    retrieveData();
                } catch (IOException e){
                    Log.e(consoleLog,"Cannot retrieve map data");
                    return;
                }
            }
        }).start();

    }

    //retrieving data from api
    protected void retrieveData() throws IOException {
        // connecting to the web service
        HttpURLConnection connection = null;
        final StringBuilder json = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            // Read the JSON data into the stringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = streamReader.read(buff)) != -1) {
                json.append(buff, 0, read);

            }

        } catch (IOException e) {
            Log.e(consoleLog, "Error connecting to service ", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    makeMarkers(json.toString());

                } catch (JSONException e) {
                    Log.e(consoleLog, "Error processing JSON", e);
                }
            }
        });
    }

    void makeMarkers(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            MarkerOptions jsonMarkers =  new MarkerOptions();
            jsonMarkers.snippet(jsonObject.getString("status"));
            if (jsonMarkers.getSnippet().equalsIgnoreCase("low")){
                jsonMarkers.icon(BitmapDescriptorFactory.fromResource(R.drawable.sgreen_bin));
            }
            else if (jsonMarkers.getSnippet().equalsIgnoreCase("medium")){
                jsonMarkers.icon(BitmapDescriptorFactory.fromResource(R.drawable.syellow_bin));
            }
            else if (jsonMarkers.getSnippet().equalsIgnoreCase("high")){
                jsonMarkers.icon(BitmapDescriptorFactory.fromResource(R.drawable.sred_bin));
            }
            mMap.addMarker(jsonMarkers
                    .title(jsonObject.getString("name"))
                    .position(new LatLng(
                            jsonObject.getJSONArray("LatLng").getDouble(0),
                            jsonObject.getJSONArray("LatLng").getDouble(1)
                    )));

        }

    }

}
