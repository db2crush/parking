package com.yalo.erunn.parking;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    Retrofit retrofit;
    ApiService apiService;

    ArrayList<Parking> parkings = new ArrayList<Parking>();

    private FrameLayout mMapview;

    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String picker = place.getLatLng().toString();

                int indexStart = picker.indexOf("(");
                int indexShimPyo = picker.indexOf(",");
                int indexLast = picker.indexOf(")");

                String stringLat = picker.substring(indexStart + 1, indexShimPyo);
                String stringLng = picker.substring(indexShimPyo + 1, indexLast);

                double pickerLat = Double.parseDouble(stringLat);
                double pickerLng = Double.parseDouble(stringLng);
                LatLng pickerGeo = new LatLng(pickerLat, pickerLng);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickerGeo, 15));

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.v("Test", status + "");

            }
        });


        mMapview = (FrameLayout) mapFragment.getView();

        retrofit = new Retrofit.Builder().baseUrl(ApiService.ApiURL).build();
        apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> parks = apiService.getParks();
        parks.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        Log.v("Test",jsonArray.length()+"AAAAAAAAAA");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            parkings.add(new Parking(jsonObject.getString("주차장명")
                                    , jsonObject.getString("소재지도로명주소")
                                    , jsonObject.getString("소재지지번주소")
                                    , jsonObject.getInt("주차구획수")
                                    , jsonObject.getString("요금정보")
                                    , jsonObject.getString("운영요일")
                            ));
                            Log.v("Test",i+"");

                            if (i == jsonArray.length()-2) {
                                Log.v("Test","if");
                                SetMarker setMarker = new SetMarker(mMap, getApplicationContext(), parkings);
                                setMarker.execute();

                            }



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng initial = new LatLng(33.485903, 126.47837);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initial, 16));

        mMap.setOnMyLocationButtonClickListener(this);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        if (mMapview != null &&
                mMapview.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mMapview.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);


        }
        enableMyLocation();

    }

    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "현재 위치를 탐색합니다.", Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Test", connectionResult + "");
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        int markerIndex = (int) marker.getZIndex();

        String name = parkings.get(markerIndex).getName();
        int quantity = parkings.get(markerIndex).getQuantity();
        String free = parkings.get(markerIndex).getFree();
        String days = parkings.get(markerIndex).getDays();

        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("quantity", quantity);
        bundle.putString("free", free);
        bundle.putString("days", days);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragment = new ParkingFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.infor_park, fragment);
        fragmentTransaction.commit();

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
}
