package com.atyourdoorteam.atyourdoor.UI.Fragments;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.SearchViewAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Activities.ProductsActivity;
import com.atyourdoorteam.atyourdoor.models.Products;
import com.atyourdoorteam.atyourdoor.models.SearchProductsByLocation.SearchProducts;
import com.atyourdoorteam.atyourdoor.models.Shop;
import com.atyourdoorteam.atyourdoor.models.SubCategoriesProducts.ProductsBySubCategories;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private SearchView searchView;
    private RecyclerView searchRecyclerView;
    private SearchViewAdapter searchViewAdapter;
    private ProgressDialog progressDialog;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView productCount;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Address> addresses;
    private int PERMISSION_ID = 44;
    private String address;
    private Geocoder geocoder;
    private List<Address> shopAddress;
    private String shopCity;
    private TextView noProductsAvailableForSearch;

    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("SEARCH PRODUCTS");

        geocoder = new Geocoder(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();

        searchView = view.findViewById(R.id.searchView);
        searchView.setVisibility(View.INVISIBLE);
        productCount = view.findViewById(R.id.productCount);
        noProductsAvailableForSearch = view.findViewById(R.id.noProductsAvailableForSearch);
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayoutForSearch);
        shimmerFrameLayout.startShimmer();

        searchRecyclerView = view.findViewById(R.id.searchProductsRecyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Loading Categories");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getProducts();
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
                                    getProducts(addresses.get(0).getLocality().trim().toLowerCase(), location.getLatitude(), location.getLongitude());
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

                Log.d("WHOLE ADDRESS", addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String latitude = String.valueOf(mLastLocation.getLatitude());
            String longitude = String.valueOf(mLastLocation.getLongitude());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getProducts(addresses.get(0).getLocality().trim().toLowerCase(), mLastLocation.getLatitude(), mLastLocation.getLongitude());
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

    private void getProducts(String userCity, double latitude, double longitude) {
        APIService apiService = RetrofitInstance.getService();

        Call<List<SearchProducts>> getAllProductsForSearch = apiService.getAllProductsForSearch();

        getAllProductsForSearch.enqueue(new Callback<List<SearchProducts>>() {
            @Override
            public void onResponse(Call<List<SearchProducts>> call, Response<List<SearchProducts>> response) {
                if (response.isSuccessful()) {
                    List<SearchProducts> searchProductsList = response.body();

                    ArrayList<SearchProducts> searchProductsArrayList = new ArrayList<>();

                    for (int j = 0; j < searchProductsList.size(); j++) {
                        Double latitude = searchProductsList.get(j).getShopId().getShopLocation().get(0);
                        Double longitude = searchProductsList.get(j).getShopId().getShopLocation().get(1);

                        try {
                            shopAddress = geocoder.getFromLocation(latitude, longitude, 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        shopCity = shopAddress.get(0).getLocality().trim().toLowerCase();
                        if (shopCity.equals(userCity)) {
                            searchProductsArrayList.add(new SearchProducts(
                                    searchProductsList.get(j).getId(),
                                    searchProductsList.get(j).getShopId(),
                                    searchProductsList.get(j).getMainCategoryId(),
                                    searchProductsList.get(j).getSubCategoryId(),
                                    searchProductsList.get(j).getProductImageURL(),
                                    searchProductsList.get(j).getProductName(),
                                    searchProductsList.get(j).getProductPrice()
                            ));
                        }
                    }

//                    for (int i = 0; i < searchProductsList.size(); i++) {
//                        searchProductsArrayList.add(new SearchProducts(
//                                searchProductsList.get(i).getId(),
//                                searchProductsList.get(i).getShopId(),
//                                searchProductsList.get(i).getMainCategoryId(),
//                                searchProductsList.get(i).getSubCategoryId(),
//                                searchProductsList.get(i).getProductImageURL(),
//                                searchProductsList.get(i).getProductName(),
//                                searchProductsList.get(i).getProductPrice()
//                        ));
//                    }
                    if (searchProductsArrayList.size() == 0) {
                        noProductsAvailableForSearch.setText("Ooh.. No..  No Products Available in Your City \uD83D\uDE25. We Will Come To" + shopCity + " Soon \uD83D\uDE04!! ");
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);
                        productCount.setVisibility(View.GONE);
                    } else {
                        productCount.setText("Search From " + searchProductsArrayList.size() + " Products of " + shopCity + " City");
                        searchViewAdapter = new SearchViewAdapter(searchProductsArrayList, getContext(), latitude, longitude);
                        searchRecyclerView.setAdapter(searchViewAdapter);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        searchView.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<SearchProducts>> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

//        getAllProductsForSearch.enqueue(new Callback<List<Products>>() {
//            @Override
//            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
//                if (response.isSuccessful()) {
//                    List<Products> productsList = response.body();
//
//                    ArrayList<Products> productsArrayList = new ArrayList<>();
//
//                    for (int i = 0; i < productsList.size(); i++) {
//                        productsArrayList.add(new Products(
//                                productsList.get(i).getId(),
//                                productsList.get(i).getShopId(),
//                                productsList.get(i).getMainCategoryId(),
//                                productsList.get(i).getSubCategoryId(),
//                                productsList.get(i).getProductImageURL(),
//                                productsList.get(i).getProductName(),
//                                productsList.get(i).getProductPrice()));
//                    }
//
//                    searchViewAdapter = new SearchViewAdapter(productsArrayList, getContext());
//                    searchRecyclerView.setAdapter(searchViewAdapter);
//                    shimmerFrameLayout.stopShimmer();
//                    shimmerFrameLayout.setVisibility(View.GONE);
//                    searchView.setVisibility(View.VISIBLE);
////                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Products>> call, Throwable t) {
////                progressDialog.dismiss();
//                shimmerFrameLayout.stopShimmer();
//                shimmerFrameLayout.setVisibility(View.GONE);
//            }
//        });
    }
}