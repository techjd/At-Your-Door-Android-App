package com.atyourdoorteam.atyourdoor.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.models.Products;
import com.atyourdoorteam.atyourdoor.models.SearchProductsByLocation.SearchProducts;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder> implements Filterable {
    private List<SearchProducts> searchProducts;
    private List<SearchProducts> searchProductsListFull;
    private ArrayList<Cart> carts;
    private Database database;
    Context context;
    double latitude;
    double longitude;

    public SearchViewAdapter(List<SearchProducts> searchProducts, Context context) {
        this.searchProducts = searchProducts;
        this.context = context;
        searchProductsListFull = new ArrayList<>(searchProducts);
    }

    public SearchViewAdapter(List<SearchProducts> searchProducts, Context context, double latitude, double longitude) {
        this.searchProducts = searchProducts;
        this.context = context;
        searchProductsListFull = new ArrayList<>(searchProducts);
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_search_products, parent, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(listItem);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        final SearchProducts search = searchProducts.get(position);
        final int[] start = {0};
        database = Room.databaseBuilder(context, Database.class, "CartDB").allowMainThreadQueries().build();

        Glide.with(context).load(search.getProductImageURL()).into(holder.productImage);
        holder.productName.setText(search.getProductName());
        holder.productPrice.setText(search.getProductPrice());
        holder.soldBy.setText(search.getShopId().getShopName());

        double shopLatitude = search.getShopId().getShopLocation().get(0);
        double shopLongitude = search.getShopId().getShopLocation().get(1);

        holder.distance.setText(Math.round(distance(latitude, shopLatitude, longitude, shopLongitude) * 100.0) / 100.0 + " Kms Far");
        carts = (ArrayList<Cart>) database.getCartDao().getProducts();

        String productId = search.getId();

        for (int i = 0; i < searchProducts.size(); i++) {
//            Log.d("PRODUCTSLIST", productsList.get(i).getId());

            for (int j = 0; j < carts.size(); j++) {
                if (carts.get(j).getProductId().equals(productId)) {
                    Log.d("MATCHED PRODUCTS", "Products Matched");
                    holder.addToCard.setVisibility(View.INVISIBLE);
                    start[0] = carts.get(j).getProductQuantity();
                    holder.number.setText(String.valueOf(carts.get(j).getProductQuantity()));
                }
            }
        }


//        Log.d("SHOPID", shopID);


        holder.addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start[0]++;
                holder.number.setText(String.valueOf(start[0]));
                Log.d("CHECKINGTHIS", search.toString());
                Cart cart = new Cart();
                cart.setProductId(search.getId());
                cart.setShopId(search.getShopId().getId());
                cart.setMainCategoryId(search.getMainCategoryId());
                cart.setSubCategoryId(search.getSubCategoryId());
                cart.setProductImageURL(search.getProductImageURL());
                cart.setProductName(search.getProductName());
                cart.setProductPrice(Integer.parseInt(search.getProductPrice()));
                cart.setProductQuantity(start[0]);
                addToCart(cart);
                holder.addToCard.setVisibility(View.INVISIBLE);


            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start[0]++;
                holder.number.setText(String.valueOf(start[0]));
                Cart cart = new Cart();
                cart.setProductId(search.getId());
                cart.setShopId(search.getShopId().getId());
                cart.setMainCategoryId(search.getMainCategoryId());
                cart.setSubCategoryId(search.getSubCategoryId());
                cart.setProductImageURL(search.getProductImageURL());
                cart.setProductName(search.getProductName());
                cart.setProductPrice(Integer.parseInt(search.getProductPrice()));
                cart.setProductQuantity(start[0]);
                updateCart(cart);
                if (start[0] == 10) {
                    Toast.makeText(v.getContext(), "Can't Add More Products", Toast.LENGTH_SHORT).show();
                    holder.plus.setEnabled(false);

                }
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start[0]--;
                if (start[0] < 1) {
                    Toast.makeText(v.getContext(), "Can't Decrease", Toast.LENGTH_SHORT).show();
                    start[0] = 0;
                    holder.plus.setEnabled(true);
                    Cart cart = new Cart();
                    cart.setProductId(search.getId());
                    cart.setShopId(search.getShopId().getId());
                    cart.setMainCategoryId(search.getMainCategoryId());
                    cart.setSubCategoryId(search.getSubCategoryId());
                    cart.setProductImageURL(search.getProductImageURL());
                    cart.setProductName(search.getProductName());
                    cart.setProductPrice(Integer.parseInt(search.getProductPrice()));
                    cart.setProductQuantity(start[0]);
                    deleteItem(cart);
                    holder.addToCard.setVisibility(View.VISIBLE);
                } else {
                    holder.number.setText(String.valueOf(start[0]));
                    Cart cart = new Cart();
                    cart.setProductId(search.getId());
                    cart.setShopId(search.getShopId().getId());
                    cart.setMainCategoryId(search.getMainCategoryId());
                    cart.setSubCategoryId(search.getSubCategoryId());
                    cart.setProductImageURL(search.getProductImageURL());
                    cart.setProductName(search.getProductName());
                    cart.setProductPrice(Integer.parseInt(search.getProductPrice()));
                    cart.setProductQuantity(start[0]);
                    updateCart(cart);
                }
            }
        });

    }

    private void addToCart(Cart cart) {
        new AddToCartAsyncTask().execute(cart);
        Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();

    }

    private void updateCart(Cart cart) {
        new EditCartDetailsAycncTask().execute(cart);
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchProducts> productsList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                productsList.addAll(searchProductsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SearchProducts products : searchProductsListFull) {
                    if (products.getProductName().toLowerCase().contains(filterPattern)) {
                        productsList.add(products);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = productsList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchProducts.clear();
            searchProducts.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private class AddToCartAsyncTask extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            database.getCartDao().insert(carts[0]);
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class EditCartDetailsAycncTask extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            database.getCartDao().update(carts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void deleteItem(Cart cartItem) {
        new DeleteItemAsyncTask().execute(cartItem);
    }

    private class DeleteItemAsyncTask extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            database.getCartDao().delete(carts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notifyDataSetChanged();
            //            getCartList();
        }
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

    @Override
    public int getItemCount() {
        return searchProducts.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName, productPrice;
        public Button addToCard;
        public ImageButton plus, minus;
        public TextView number;
        public TextView soldBy;
        public TextView distance;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCard = itemView.findViewById(R.id.addToCart);
            plus = itemView.findViewById(R.id.add);
            minus = itemView.findViewById(R.id.remove);
            number = itemView.findViewById(R.id.Number);
            soldBy = itemView.findViewById(R.id.soldBy);
            distance = itemView.findViewById(R.id.shopDistance);
        }
    }
}
