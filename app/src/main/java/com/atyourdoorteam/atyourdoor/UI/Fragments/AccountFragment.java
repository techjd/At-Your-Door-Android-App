package com.atyourdoorteam.atyourdoor.UI.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Activities.AddressActivity;
import com.atyourdoorteam.atyourdoor.UI.Activities.LoginActivity;
import com.atyourdoorteam.atyourdoor.UI.Activities.OrdersActivity;
import com.atyourdoorteam.atyourdoor.models.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    //    private Toolbar toolbar;
//    private TextView toolbar_title;
    private TextView userName, emailAddress;
    private TextView creditNumber;
    private ProgressDialog progressDialog;
    private CardView addressCardView;
    private CardView ourIdeaAndMoto, ordersCardView, privacyPolicyCardView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");

//        toolbar = view.findViewById(R.id.toolbar);
//        toolbar_title = view.findViewById(R.id.toolbar_title);
//        toolbar_title.setText("PROFILE");
        userName = view.findViewById(R.id.userName);
        emailAddress = view.findViewById(R.id.emailAddress);
        addressCardView = view.findViewById(R.id.cardView5);
        ourIdeaAndMoto = view.findViewById(R.id.cardView2);
        ordersCardView = view.findViewById(R.id.cardView1);

        creditNumber = view.findViewById(R.id.creditNumber);

        addressCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAddressActivity = new Intent(getContext(), AddressActivity.class);
                startActivity(openAddressActivity);
            }
        });

        ourIdeaAndMoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ordersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToOrdersActivity = new Intent(getContext(), OrdersActivity.class);
                startActivity(navigateToOrdersActivity);
            }
        });
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Profile Information");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        APIService apiService = RetrofitInstance.getService();


        Call<UserInfo> userInfoCall = apiService.getLoggedInUserInfo(token);

        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    String email = response.body().getEmail();
                    String username = response.body().getName();
                    String credit = response.body().getCredit();
                    Log.d("EMAIL", response.body().getEmail());

                    userName.setText(username);
                    emailAddress.setText(email);
                    creditNumber.setText(credit);
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please Try Again ! Serve Side Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
