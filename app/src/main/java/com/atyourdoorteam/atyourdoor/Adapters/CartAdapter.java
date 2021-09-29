package com.atyourdoorteam.atyourdoor.Adapters;

import android.content.Context;
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
import com.atyourdoorteam.atyourdoor.UI.Fragments.GroceyListFragment;
import com.atyourdoorteam.atyourdoor.UI.Fragments.ShoppingCartFragment;

import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Cart> cartList;
    private int total = 0;
    Context context;
    private Database database;
    private TextView totalPrice;
    private ArrayList<Cart> cartArrayListforDB;


    public CartAdapter(ArrayList<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    public CartAdapter(ArrayList<Cart> cartList, Context context, TextView totalPrice) {
        this.cartList = cartList;
        this.context = context;
        this.totalPrice = totalPrice;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_cart, parent, false);
        CartViewHolder cartViewHolder = new CartViewHolder(listItem);

        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        final Cart cart = cartList.get(position);
        final int[] start = {0};
        database = Room.databaseBuilder(context, Database.class, "CartDB").allowMainThreadQueries().build();
        int getTotal = database.getCartDao().getTotal();
        Log.d("NUMBER", String.valueOf(getTotal));
        String productId = cart.getProductId();
//        updateTotalPrice();
        Glide.with(context).load(cart.getProductImageURL()).into(holder.productImage);
        holder.productName.setText(cart.getProductName());
        holder.productPrice.setText(String.valueOf(cart.getProductPrice()));
        holder.productQuantity.setText(String.valueOf(cart.getProductQuantity()));


        for (int j = 0; j < cartList.size(); j++) {
            if (cartList.get(j).getProductId().equals(productId)) {
                Log.d("MATCHED PRODUCTS", "Products Matched");
                holder.addToCard.setVisibility(View.INVISIBLE);
                start[0] = cartList.get(j).getProductQuantity();
                holder.number.setText(String.valueOf(cartList.get(j).getProductQuantity()));
            }
        }


        holder.addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start[0]++;
                holder.number.setText(String.valueOf(start[0]));
                holder.productQuantity.setText(String.valueOf(start[0]));
                Log.d("CHECKINGTHIS", cartList.toString());
                Cart cart1 = new Cart();
                cart1.setProductId(cart.getProductId());
                cart1.setShopId(cart.getShopId());
                cart1.setMainCategoryId(cart.getMainCategoryId());
                cart1.setSubCategoryId(cart.getSubCategoryId());
                cart1.setProductImageURL(cart.getProductImageURL());
                cart1.setProductName(cart.getProductName());
                cart1.setProductPrice(cart.getProductPrice());
                cart1.setProductQuantity(start[0]);
//                addToCart(cart);
                holder.addToCard.setVisibility(View.INVISIBLE);

            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start[0]++;
                holder.number.setText(String.valueOf(start[0]));
                holder.productQuantity.setText(String.valueOf(start[0]));
                Cart cart1 = new Cart();
                cart1.setProductId(cart.getProductId());
                cart1.setShopId(cart.getShopId());
                cart1.setMainCategoryId(cart.getMainCategoryId());
                cart1.setSubCategoryId(cart.getSubCategoryId());
                cart1.setProductImageURL(cart.getProductImageURL());
                cart1.setProductName(cart.getProductName());
                cart1.setProductPrice(cart.getProductPrice());
                cart1.setProductQuantity(start[0]);

                updateCart(cart1);

//                ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ShoppingCartFragment()).commit();

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
                    Cart cart1 = new Cart();
                    cart1.setProductId(cart.getProductId());
                    cart1.setShopId(cart.getShopId());
                    cart1.setMainCategoryId(cart.getMainCategoryId());
                    cart1.setSubCategoryId(cart.getSubCategoryId());
                    cart1.setProductImageURL(cart.getProductImageURL());
                    cart1.setProductName(cart.getProductName());
                    cart1.setProductPrice(cart.getProductPrice());
                    cart1.setProductQuantity(start[0]);
                    deleteItem(cart);

//                    ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            new ShoppingCartFragment()).commit();
                    holder.addToCard.setVisibility(View.VISIBLE);
                } else {
                    holder.plus.setEnabled(true);
                    holder.number.setText(String.valueOf(start[0]));
                    holder.productQuantity.setText(String.valueOf(start[0]));
                    Cart cart1 = new Cart();
                    cart1.setProductId(cart.getProductId());
                    cart1.setShopId(cart.getShopId());
                    cart1.setMainCategoryId(cart.getMainCategoryId());
                    cart1.setSubCategoryId(cart.getSubCategoryId());
                    cart1.setProductImageURL(cart.getProductImageURL());
                    cart1.setProductName(cart.getProductName());
                    cart1.setProductPrice(cart.getProductPrice());
                    cart1.setProductQuantity(start[0]);

                    updateCart(cart1);

//                    ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            new ShoppingCartFragment()).commit();
                }
            }
        });


    }


    private void updateCart(Cart cart) {
        new EditCartDetailsAycncTask().execute(cart);
    }

    private class EditCartDetailsAycncTask extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            Log.d("WHATS GOINF ER", String.valueOf(carts[0].getProductQuantity()));
            database.getCartDao().update(carts[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getAllItems();
            getTotalPrice();
//            setCarts(cartList);


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
//                setCarts((ArrayList<Cart>) database.getCartDao().getProducts());
//            updateTotalPrice((ArrayList<Cart>) database.getCartDao().getProducts());
            getAllItems();
            getTotalPrice();
//            getCartList();
        }
    }

    private void getTotalPrice() {
        new getTotalPriceAsyncTask().execute();
    }

    private class getTotalPriceAsyncTask extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... voids) {
            int total = database.getCartDao().getTotal();
            return total;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d("THIS IS THE TOTAL", String.valueOf(integer));

            totalPrice.setText(String.valueOf(integer));
        }
    }

    void getAllItems() {
        new GetAllCartItemsAsyncTask().execute();
    }

    private class GetAllCartItemsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            cartArrayListforDB = (ArrayList<Cart>) database.getCartDao().getProducts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setCarts(cartArrayListforDB);
        }
    }


    @Override
    public int getItemCount() {
        if (cartList != null) {
            return cartList.size();
        } else {
        }
        return 0;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.cartList = carts;
//        updateTotalPrice(cartList);

        notifyDataSetChanged();
    }

//    private void updateTotalPrice(ArrayList<Cart> cartArrayList) {
//        new UpdateTotalPrice().execute(cartArrayList);
//    }
//
//    private class UpdateTotalPrice extends AsyncTask<ArrayList<Cart>, Void, Integer> {
//
//
//        @Override
//        protected Integer doInBackground(ArrayList<Cart>... arrayLists) {
//            for (int i = 0; i < arrayLists[0].size(); i++) {
//                int productPrice = Integer.parseInt(arrayLists[0].get(i).getProductPrice());
//                int quantity = arrayLists[0].get(i).getProductQuantity();
//
//                total += productPrice * quantity;
//            }
//            return total;
//        }
//
//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//            Log.d("TOTALPRICE", String.valueOf(total));
//            totalPrice.setText(String.valueOf(total));
//            getAllItems();
//        }
//    }

//    private void updateTotalPrice() {
//        new UpdateTotalPrice().execute();
//    }
//
//    private class UpdateTotalPrice extends AsyncTask<Void, Void, Integer> {
//
//
//        @Override
//        protected Integer doInBackground(Void... voids) {
//            database = Room.databaseBuilder(context, Database.class, "CartDB").build();
//            ArrayList<Cart> cartArrayList = new ArrayList<>();
//            cartArrayList = (ArrayList<Cart>) database.getCartDao().getProducts();
//            for (int i = 0; i < cartArrayList.size(); i++) {
//                int productPrice = cartArrayList.get(i).getProductPrice();
//                int quantity = cartArrayList.get(i).getProductQuantity();
//
//                total += productPrice * quantity;
//            }
//            return total;
//        }
//
//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//            Log.d("TOTALPRICE", String.valueOf(total));
//
//
//        }
//    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName, productPrice, productQuantity;
        public Button addToCard;
        public ImageButton plus, minus;
        public Button edit;
        public TextView number;
        public Context context;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();


            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.quantity);
            addToCard = itemView.findViewById(R.id.addToCart);
            plus = itemView.findViewById(R.id.add);
            minus = itemView.findViewById(R.id.remove);
            number = itemView.findViewById(R.id.Number);


        }

    }


}
