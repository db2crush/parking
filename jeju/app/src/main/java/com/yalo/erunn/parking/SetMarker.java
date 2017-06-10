package com.yalo.erunn.parking;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by erunn on 2017-06-06.
 */

public class SetMarker extends AsyncTask<Void, Integer, Void> {
    GoogleMap mMap;
    Context context;
    ArrayList<Parking> parkings;

    Geocoder mGeoCoder;
    String address, name;
    double lat, lng;
    LatLng park = null;
    List<Address> addrs = null;


    SetMarker(GoogleMap map, Context context, ArrayList<Parking> parkings) {
        this.mMap = map;
        this.context = context;
        this.parkings = parkings;
        this.mGeoCoder = new Geocoder(context, Locale.KOREA);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.v("Test",values[0]+"");
        super.onProgressUpdate(values);
        park = new LatLng(lat, lng);

        mMap.addMarker(new MarkerOptions()
                .zIndex(values[0])
                .position(park)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.v("index",name+" " +"doinbackground");
        for (int i = 0; i < parkings.size(); i++) {
            address = parkings.get(i).getAddress();
            name = parkings.get(i).getName();
            try {
                addrs = mGeoCoder.getFromLocationName(address, 1);

                if (!addrs.isEmpty()) {
                    lat = addrs.get(0).getLatitude();
                    lng = addrs.get(0).getLongitude();
                    addrs = null;
                    publishProgress(i);
                } else
                    continue;

            } catch (IOException e) {
                Log.v("error", e + "");
            }


        }
        return null;
    }
}
