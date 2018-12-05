package com.example.divyanshsingh.transportationmanagement.acitivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.divyanshsingh.transportationmanagement.R;
import com.example.divyanshsingh.transportationmanagement.parser.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VehicleDetail extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 99;
    private GoogleMap mMap;
    Activity activity = this;
    ArrayList<LatLng> locs = new ArrayList<LatLng>();//pass and add coordinates of waypoints on route

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map); // Get the SupportMapFragment and request notification
        //Add coordinates of waypoints
        locs.add(new LatLng(26.457273, 80.349943));
        locs.add(new LatLng(26.477538, 80.343243));
        locs.add(new LatLng(26.480797, 80.301492));
        locs.add(new LatLng(26.481447, 80.336281));
        locs.add(new LatLng(26.475689, 80.289273));
        mapFragment.getMapAsync(this);// a callback object which will be triggered when the GoogleMap instance is ready to be used.
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();// builder that is able to create a minimum bound or rectangle based on a set of LatLng points.
        for (int i = 0; i < locs.size(); i++)
        {
            mMap.addMarker(new MarkerOptions().position(locs.get(i)).title("Marker" + i));//adding markers on all waypoints
            builder.include(locs.get(i));//Including each waypoint for building of the bounds.
        }
        LatLngBounds bounds = builder.build();//Creates the LatLng bounds.
        int padding = 50; // offset from edges of the map in pixels
        //Returns a CameraUpdate that transforms the camera such that the specified latitude/longitude bounds are centered on screen at the greatest possible zoom level.
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);
            }
        });// modify a map's camera

        String url = getDirectionsUrl(locs.get(0), locs.get(locs.size() - 1));
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
        checkPermission();//check for location permission at runtime

    }
    void checkPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);

        } else {

            updatemap();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updatemap();//sets 'my location' button
            } else {
                // Permission was denied. Display an error message.
            }
        }

    }

    @SuppressLint("MissingPermission")
    void updatemap() {
        mMap.setMyLocationEnabled(true);//enable the My Location layer on the map and add 'my location' button on top right corner
        mMap.setOnMyLocationButtonClickListener(this);//enable a listener on 'my location' button



    }
    @Override
    public boolean onMyLocationButtonClick() {
        isGPSEnable();//display a dialog box to configure gps setting
        return false;
    }

    public void isGPSEnable() {

        LocationManager mlocManager;
        mlocManager = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);

        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    activity);
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    activity.startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);//parse string to create JSONObjects and further extract necessary data

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;//List of route , where each route has a list of coordinate points represented as Hash Map

            try {
                jObject = new JSONObject(jsonData[0]);//creating JSON object of the response
                DirectionsJSONParser parser = new DirectionsJSONParser(activity);

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;//Defines options for a polyline(line representing resultant route) which will be drawn on map.


            for (int i = 0; i < result.size(); i++) {

                points = new ArrayList();//List of all location points constituting the route
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);//obtain route

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);//obtain location point

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);//Adds vertices to the end of the polyline being built.
                lineOptions.width(12);//Sets the width of the polyline in screen pixels.
                lineOptions.color(Color.RED);//Sets the color of the polyline
                lineOptions.geodesic(true);// compute the shortest path
                mMap.addPolyline(lineOptions);//draw the route line on map
            }


        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";//computing route only for vehicles
        String key = "key=AIzaSyCMQFmvLLCPawrJO0mb9NTjP2bxiS8sQ9Y";//Server side API key
        String alternatives = "alternatives=false";//compute single best route for given waypoints, no other alternative route is calculated
        String waypoints = "waypoints=optimize%3Atrue";//'optimize = true' means arrange waypoints in such manner quickest and shortest route is computed
        for (int i = 1; i < locs.size() - 1; i++)//except source and destination, all other major points is considered waypoints
        {
            waypoints = waypoints + "%7C" + locs.get(i).latitude + "%2C" + locs.get(i).longitude;//ASCII keycode in hexadecimal '7C' = '|' and '2C' = ','
        }
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + alternatives + "&" + waypoints + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service or Direction API request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            // This is getting the url from the string we passed in
            URL url = new URL(strUrl);
            // Create the urlConnection
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();//Direction API response in JSON format
            Log.e("downloadUrl: ", data);

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


}
