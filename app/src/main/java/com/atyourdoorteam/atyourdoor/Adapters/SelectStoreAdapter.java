package com.atyourdoorteam.atyourdoor.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Activities.ShopCategoriesActivity;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.entities.GroceryList;
import com.atyourdoorteam.atyourdoor.models.Products;
import com.atyourdoorteam.atyourdoor.models.Shop;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectStoreAdapter extends RecyclerView.Adapter<SelectStoreAdapter.SelectStoreViewHolder> {

    private List<Shop> shopList;
    private Context context;
    private Database database;
    private Database cartDb;
    private ArrayList<GroceryList> groceryLists;
    private ArrayList<String> finalList = new ArrayList<>();
    private double latitude;
    private double longitude;

    public SelectStoreAdapter(List<Shop> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
    }

    public SelectStoreAdapter(List<Shop> shopList, Context context, double latitude, double longitude) {
        this.shopList = shopList;
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public SelectStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_shop, parent, false);
        SelectStoreViewHolder selectStoreViewHolder = new SelectStoreViewHolder(listItem);
        return selectStoreViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectStoreViewHolder holder, int position) {
        final Shop shop = shopList.get(position);

        Glide.with(context).load(shop.getShopImageURL()).into(holder.shopsImage);
        holder.shopsTitle.setText(shop.getShopName());
        holder.shopsAddress.setText(shop.getShopAddress());
        double shoplatitude = shop.getShopLocation().get(0);
        double shoplongitude = shop.getShopLocation().get(1);
        holder.shopDistance.setText(Math.round(distance(latitude, shoplatitude, longitude, shoplongitude) * 100.0) / 100.0 + " Kms Far");
        database = Room.databaseBuilder(context, Database.class, "ListDB").allowMainThreadQueries().build();
        cartDb = Room.databaseBuilder(context, Database.class, "CartDB").allowMainThreadQueries().build();
        groceryLists = (ArrayList<GroceryList>) database.getGroceryDao().getGroceryList();

        holder.shopCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopId = shop.getId();
                for (int i = 0; i < groceryLists.size(); i++) {
                    Log.d("LISTS", groceryLists.get(i).getProductName());
                }
                final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setTitle("Finding And Adding");
                progressDialog.setMessage("Please Wait , It May take some time");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                APIService apiService = RetrofitInstance.getService();

                Call<List<Products>> productsList = apiService.getProductsList(shopId);
                productsList.enqueue(new Callback<List<Products>>() {
                    @Override
                    public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                        if (response.isSuccessful()) {
                            List<Products> products = response.body();

                            ArrayList<Products> productsArrayList = new ArrayList<>();

//                            ArrayList<String> productNames = new ArrayList<>();

                            HashMap<String, ArrayList<Integer>> wordList = new HashMap<>();
                            String[] words;
                            for (int j = 0; j < products.size(); j++) {
//                                productNames.add(products.get(j).getProductName());
                                productsArrayList.add(new Products(products.get(j).getId(), products.get(j).getProductName()));
                            }

                            for (int i = 0; i < productsArrayList.size(); i++) {

                                words = productsArrayList.get(i).getProductName().trim().toLowerCase().split(" ");

                                for (String word : words) {
                                    if (wordList.containsKey(word)) wordList.get(word).add(i);
                                    else {
                                        ArrayList<Integer> list = new ArrayList<>();
                                        list.add(i);
                                        wordList.put(word, list);
                                    }
                                }
                            }

                            for (int z = 0; z < groceryLists.size(); z++) {

                                String query = groceryLists.get(z).getProductName().toLowerCase();
                                Log.d("QUERY", query);
                                words = query.split(" ");
                                ArrayList<Set<Integer>> productSets = new ArrayList<>();
                                for (String word : words) {
                                    if (wordList.containsKey(word)) {
                                        productSets.add(new HashSet<>(wordList.get(word)));
                                    }
                                }

                                if (productSets.size() == 0) {
                                    Log.d("NF", "Nothing Found");
                                } else {
                                    Set<Integer> result = productSets.get(0);
                                    for (int i = 1; i < productSets.size(); i++) {
                                        result.addAll(productSets.get(i));
                                    }

                                    for (Integer i : result) {
//                                        Log.d("UNION", productNames.get(i));
                                        Log.d("UNION", productsArrayList.get(i).getId());
                                        finalList.add(productsArrayList.get(i).getId());
                                    }
                                }

                            }

//                            for (int j = 0; j < finalList.size(); j++) {
//                                Log.d("FINAL LIST", finalList.get(j));
//                            }

                            Set<String> set = new HashSet<>(finalList);
                            finalList.clear();
                            finalList.addAll(set);
                            for (String s : set) {
                                Log.d("FINAL LIST TO ADD", s);
                                Call<Products> getASingleProduct = apiService.getAsingleProduct(s);
                                getASingleProduct.enqueue(new Callback<Products>() {
                                    @Override
                                    public void onResponse(Call<Products> call, Response<Products> response) {
                                        if (response.isSuccessful()) {
                                            Products getProduct = response.body();

                                            Log.d("PRODUCT NAME AND PRICE", getProduct.getProductName() + " " + getProduct.getProductPrice());

                                            Cart cart = new Cart();
                                            cart.setProductId(getProduct.getId());
                                            cart.setShopId(getProduct.getShopId());
                                            cart.setMainCategoryId(getProduct.getMainCategoryId());
                                            cart.setSubCategoryId(getProduct.getSubCategoryId());
                                            cart.setProductImageURL(getProduct.getProductImageURL());
                                            cart.setProductName(getProduct.getProductName());
                                            cart.setProductPrice(Integer.parseInt(getProduct.getProductPrice()));
                                            cart.setProductQuantity(1);
                                            addToCart(cart);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Products> call, Throwable t) {

                                    }
                                });
                            }


                            progressDialog.dismiss();
                            Toast.makeText(v.getContext(), "Related Items Added To Cart", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Products>> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void addToCart(Cart cart) {
        new AddToCartAsyncTask().execute(cart);
    }

    private class AddToCartAsyncTask extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            cartDb.getCartDao().insert(carts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }

    public static class SelectStoreViewHolder extends RecyclerView.ViewHolder {
        public ImageView shopsImage;
        public TextView shopsTitle;
        public TextView shopsAddress;
        public CardView shopCardView;
        public TextView shopDistance;

        public SelectStoreViewHolder(@NonNull View itemView) {
            super(itemView);

            shopsImage = itemView.findViewById(R.id.shopImage);
            shopsTitle = itemView.findViewById(R.id.shopName);
            shopsAddress = itemView.findViewById(R.id.shopAddress);
            shopCardView = itemView.findViewById(R.id.shopCardView);
            shopDistance = itemView.findViewById(R.id.shopDistance);
        }
    }
}
