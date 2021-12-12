package com.zeroonelab.www.shelter;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

class MyClassListener implements LocationListener {

    Context context;
    CurrentLocation currentLocation;

    MyClassListener (Context context)
    {
        currentLocation = new CurrentLocation();
        this.context = context;
    }

    @Override
    public void onLocationChanged(final Location location) {

    }
};

