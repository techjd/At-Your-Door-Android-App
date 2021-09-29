package com.atyourdoorteam.atyourdoor.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Fragments.ShoppingCartFragment;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.atyourdoorteam.atyourdoor.models.Products;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private List<Products> productsList;
    private ArrayList<Cart> carts;
    private Database database;

    Context context;
    String shopId;

    public ProductsAdapter(List<Products> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }



    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_product, parent, false);
        ProductsViewHolder productsViewHolder = new ProductsViewHolder(listItem);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        final Products products = productsList.get(position);
        final int[] start = {0};
        database = Room.databaseBuilder(context, Database.class, "CartDB").allowMainThreadQueries().build();

        Glide.with(context).load(products.getProductImageURL()).into(holder.productImage);
        holder.productName.setText(products.getProductName());
        holder.productPrice.setText(products.getProductPrice());


        carts = (ArrayList<Cart>) database.getCartDao().getProducts();

        String productId = products.getId();

        for (int i = 0; i < productsList.size(); i++) {
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
                Log.d("CHECKINGTHIS", products.toString());
                Cart cart = new Cart();
                cart.setProductId(products.getId());
                cart.setShopId(products.getShopId());
                cart.setMainCategoryId(products.getMainCategoryId());
                cart.setSubCategoryId(products.getSubCategoryId());
                cart.setProductImageURL(products.getProductImageURL());
                cart.setProductName(products.getProductName());
                cart.setProductPrice(Integer.parseInt(products.getProductPrice()));
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
                cart.setProductId(products.getId());
                cart.setShopId(products.getShopId());
                cart.setMainCategoryId(products.getMainCategoryId());
                cart.setSubCategoryId(products.getSubCategoryId());
                cart.setProductImageURL(products.getProductImageURL());
                cart.setProductName(products.getProductName());
                cart.setProductPrice(Integer.parseInt(products.getProductPrice()));
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
                    cart.setProductId(products.getId());
                    cart.setShopId(products.getShopId());
                    cart.setMainCategoryId(products.getMainCategoryId());
                    cart.setSubCategoryId(products.getSubCategoryId());
                    cart.setProductImageURL(products.getProductImageURL());
                    cart.setProductName(products.getProductName());
                    cart.setProductPrice(Integer.parseInt(products.getProductPrice()));
                    cart.setProductQuantity(start[0]);
                    deleteItem(cart);
                    holder.addToCard.setVisibility(View.VISIBLE);
                } else {
                    holder.number.setText(String.valueOf(start[0]));
                    Cart cart = new Cart();
                    cart.setProductId(products.getId());
                    cart.setShopId(products.getShopId());
                    cart.setMainCategoryId(products.getMainCategoryId());
                    cart.setSubCategoryId(products.getSubCategoryId());
                    cart.setProductImageURL(products.getProductImageURL());
                    cart.setProductName(products.getProductName());
                    cart.setProductPrice(Integer.parseInt(products.getProductPrice()));
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




    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public static class ProductsViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName, productPrice;
        public Button addToCard;
        public ImageButton plus, minus;
        public TextView number;
        public List<Products> productsList;
        public List<Cart> carts;


        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCard = itemView.findViewById(R.id.addToCart);
            plus = itemView.findViewById(R.id.add);
            minus = itemView.findViewById(R.id.remove);
            number = itemView.findViewById(R.id.Number);


        }




    }
}
