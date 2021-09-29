package com.atyourdoorteam.atyourdoor.UI.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.CategoriesAdapter;
import com.atyourdoorteam.atyourdoor.Adapters.ShopsAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Activities.AllCategoriesActivity;
import com.atyourdoorteam.atyourdoor.UI.Activities.AllShopsActivity;
import com.atyourdoorteam.atyourdoor.models.Categories;
import com.atyourdoorteam.atyourdoor.models.Shop;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final int REQUEST_LOCATION = 1;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView shopsRecyclerView;
    private ShopsAdapter shopsAdapter;
    private CategoriesAdapter categoriesAdapter;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView viewAllCategories, viewAllShops;
    private GoogleMap googleMap;
    private SearchView searchView;
    private ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayout2;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Address> addresses;
    private int PERMISSION_ID = 44;
    private String address;
    private Geocoder geocoder;
    private int REQUEST_CHECK_CODE = 8989;
    private LocationSettingsRequest.Builder builder;
    private TextView displayAddress;
    private TextView numberOfShops;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("AT YOUR DOOR");
        geocoder = new Geocoder(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();
        displayAddress = view.findViewById(R.id.displayAddress);
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        shimmerFrameLayout2 = view.findViewById(R.id.shimmerLayout2);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout2.startShimmer();
        categoriesRecyclerView = view.findViewById(R.id.categoriesReyclerView);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        shopsRecyclerView = view.findViewById(R.id.shopsRecyclerView);
        shopsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewAllCategories = view.findViewById(R.id.viewAllCategories);
        viewAllShops = view.findViewById(R.id.viewAllShops);
        numberOfShops = view.findViewById(R.id.nearbyShopCount);
//        searchView = view.findViewById(R.id.searchView);


        viewAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCategory = new Intent(getContext(), AllCategoriesActivity.class);
                startActivity(openCategory);
            }
        });
        viewAllShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openShops = new Intent(getContext(), AllShopsActivity.class);
                startActivity(openShops);
            }
        });
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Loading Categories And Shops");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();


//        getCategories();
//        getShops();


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
                                displayAddress.setText(address + " , " + addresses.get(0).getLocality());
                                Log.d("WHOLE ADDRESS", addresses.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String latitude = String.valueOf(location.getLatitude());
                            String longitude = String.valueOf(location.getLongitude());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getCategories();
                                    getShops(latitude, longitude);
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

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
                displayAddress.setText(address + " , " + addresses.get(0).getLocality());
                Log.d("WHOLE ADDRESS", addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String latitude = String.valueOf(mLastLocation.getLatitude());
            String longitude = String.valueOf(mLastLocation.getLongitude());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCategories();
                    getShops(latitude, longitude);
                }
            }, 700);
        }
    };

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

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

    private void getCategories() {
        APIService apiService = RetrofitInstance.getService();

        Call<List<Categories>> categoriesList = apiService.getCategoriesList();

        categoriesList.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                List<Categories> categoriesList1 = response.body();

                ArrayList<Categories> categoriesArrayList = new ArrayList<>();

                for (int i = 0; i < categoriesList1.size(); i++) {

                    categoriesArrayList.add(new Categories(categoriesList1.get(i).getCategoryName(),
                            categoriesList1.get(i).getCategoriesImage()));
                }

                categoriesAdapter = new CategoriesAdapter(categoriesArrayList, getContext());
                categoriesRecyclerView.setAdapter(categoriesAdapter);

            }


            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {

            }
        });
    }

    private void getShops(String latitude, String longitude) {
        APIService apiService = RetrofitInstance.getService();

        Call<List<Shop>> shopsList = apiService.getShopsAccrodingToLocation(longitude, latitude);

        shopsList.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                if (response.isSuccessful()) {
                    List<Shop> shopList = response.body();

                    ArrayList<Shop> shopArrayList = new ArrayList<>();

                    for (int i = 0; i < shopList.size(); i++) {
                        shopArrayList.add(new Shop(
                                shopList.get(i).getId(),
                                shopList.get(i).getShopName(),
                                shopList.get(i).getShopImageURL(),
                                shopList.get(i).getShopAddress(),
                                shopList.get(i).getShopLocation()));
                    }

                    numberOfShops.setText(shopList.size() + " Shops Around You");

                    shopsAdapter = new ShopsAdapter(shopArrayList, getContext(), Double.valueOf(latitude), Double.valueOf(longitude));
                    shopsRecyclerView.setAdapter(shopsAdapter);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout2.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout2.setVisibility(View.GONE);
//                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {

            }
        });
    }
}
