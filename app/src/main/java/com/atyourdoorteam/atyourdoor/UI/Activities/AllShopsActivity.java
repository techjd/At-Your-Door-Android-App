package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.AllShopsAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.models.Shop;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllShopsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView allShopsRecyclerView;
    private AllShopsAdapter allShopsAdapter;
    private ProgressDialog progressDialog;
    private ShimmerFrameLayout shimmerFrameLayout;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Address> addresses;
    private int PERMISSION_ID = 44;
    private String address;
    private Geocoder geocoder;
    private int REQUEST_CHECK_CODE = 8989;
    private LocationSettingsRequest.Builder builder;
    private String latitude, longitude;
    private List<Address> shopAddress;
    private String shopCity;
    private TextView inShopCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shops);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("ALL SHOPS OF YOUR CITY");

        geocoder = new Geocoder(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        inShopCount = findViewById(R.id.inCityShopCount);
        shimmerFrameLayout = findViewById(R.id.shimmerLayoutForAllShops);
        shimmerFrameLayout.startShimmer();

        allShopsRecyclerView = findViewById(R.id.shopsActivityRecyclerView);
        allShopsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getShops();
//            }
//        }, 700);


    }

    private void getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Log.d("LOCATIONS", "LAT: " + location.getLatitude() + "LONG: " + location.getLongitude());
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                address = addresses.get(0).getSubLocality();
                                Log.d("Address: ", " " + address + " , " + addresses.get(0).getLocality());

                                Log.d("WHOLE ADDRESS", addresses.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String latitude = String.valueOf(location.getLatitude());
                            String longitude = String.valueOf(location.getLongitude());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getShops(addresses.get(0).getLocality().trim().toLowerCase(), location.getLatitude(), location.getLongitude());
                                }
                            }, 700);
                        }
                    }
                });
            } else {
                //                 Location Dialog box permission
//                LocationRequest request = new LocationRequest()
//                        .setFastestInterval(1500)
//                        .setInterval(3000)
//                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//                builder = new LocationSettingsRequest.Builder()
//                        .addLocationRequest(request);
//
//                Task<LocationSettingsResponse> result =
//                        LocationServices.getSettingsClient(getContext()).checkLocationSettings(builder.build());
//
//                result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
//                        try {
//                            task.getResult(ApiException.class);
//                        } catch (ApiException e) {
//                            switch (e.getStatusCode()) {
//                                case LocationSettingsStatusCodes
//                                        .RESOLUTION_REQUIRED:
//                                    try {
//                                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                                        resolvableApiException.startResolutionForResult(getActivity(), REQUEST_CHECK_CODE);
//                                    } catch (IntentSender.SendIntentException ex) {
//                                        ex.printStackTrace();
//                                    } catch (ClassCastException ex) {
//
//                                    }
//                                    break;
//                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
//                                    break;
//                                }
//
//                            }
//                        }
//                    }
//                });
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.d("LOCATIONS", "LAT: " + mLastLocation.getLatitude() + "LONG: " + mLastLocation.getLongitude());
            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                address = addresses.get(0).getSubLocality();
                Log.d("Address: ", " " + address + " , " + addresses.get(0).getLocality());

                Log.d("WHOLE ADDRESS", addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String latitude = String.valueOf(mLastLocation.getLatitude());
            String longitude = String.valueOf(mLastLocation.getLongitude());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getShops(addresses.get(0).getLocality().trim().toLowerCase(), mLastLocation.getLatitude(), mLastLocation.getLongitude());
                }
            }, 700);
        }
    };

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermission()) {
            getLastLocation();
        }
    }


    private void getShops(String userCity, double latitude, double longitude) {
        APIService apiService = RetrofitInstance.getService();
        Call<List<Shop>> shopCall = apiService.getShopsList();
//        progressDialog = new ProgressDialog(AllShopsActivity.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Loading Shops Of Your City");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();


        shopCall.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                if (response.isSuccessful()) {

                    List<Shop> shopList = response.body();


                    ArrayList<Shop> shopArrayList = new ArrayList<>();

                    for (int j = 0; j < shopList.size(); j++) {
                        Double latitude = shopList.get(j).getShopLocation().get(0);
                        Double longitude = shopList.get(j).getShopLocation().get(1);

                        try {
                            shopAddress = geocoder.getFromLocation(latitude, longitude, 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        shopCity = shopAddress.get(0).getLocality().trim().toLowerCase();
                        if (shopCity.equals(userCity)) {
                            shopArrayList.add(new Shop(
                                    shopList.get(j).getId(),
                                    shopList.get(j).getShopName(),
                                    shopList.get(j).getShopImageURL(),
                                    shopList.get(j).getShopAddress(),
                                    shopList.get(j).getShopLocation()));
                        }
                    }

                    inShopCount.setText(shopArrayList.size() + " Shops In Your City");
//                    for (int i = 0; i < shopList.size(); i++) {
//                        shopArrayList.add(new Shop(
//                                shopList.get(i).getId(),
//                                shopList.get(i).getShopName(),
//                                shopList.get(i).getShopImageURL(),
//                                shopList.get(i).getShopAddress()));
//                    }


                    allShopsAdapter = new AllShopsAdapter(shopArrayList, AllShopsActivity.this, latitude, longitude);
                    allShopsRecyclerView.setAdapter(allShopsAdapter);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
//                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }
}