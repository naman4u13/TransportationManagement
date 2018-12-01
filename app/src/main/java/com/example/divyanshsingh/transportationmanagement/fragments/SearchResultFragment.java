package com.example.divyanshsingh.transportationmanagement.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.divyanshsingh.transportationmanagement.R;
import com.example.divyanshsingh.transportationmanagement.acitivity.VehicleDetail;

public class SearchResultFragment extends Fragment {

    private TextView vehicleName;
    public SearchResultFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        vehicleName = view.findViewById(R.id.vehicles_name);
        vehicleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VehicleDetail.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
