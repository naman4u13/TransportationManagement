package com.example.divyanshsingh.transportationmanagement.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.divyanshsingh.transportationmanagement.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class CurrentFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    private static final int MY_LOCATION_REQUEST_CODE = 99;
    ArrayList<LatLng> locs = new ArrayList<LatLng>();
    GoogleMap mMap;

    public CurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_current, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.current_map);


        locs.add(new LatLng(26.457273, 80.349943));
        locs.add(new LatLng(26.477538, 80.343243));
        locs.add(new LatLng(26.480797, 80.301492));
        mapFragment.getMapAsync(this);// a callback object which will be triggered when the GoogleMap instance is ready to be used.
        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();//builder that is able to create a minimum bound or rectangle based on a set of LatLng points.
        for (int i = 0; i < locs.size(); i++) {
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
        checkPermission();//check for location permission at runtime
    }

    void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);

        } else {

            updatemap();//sets 'my location' button
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
        mlocManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);

        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getActivity().startActivity(callGPSSettingIntent);
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
}
