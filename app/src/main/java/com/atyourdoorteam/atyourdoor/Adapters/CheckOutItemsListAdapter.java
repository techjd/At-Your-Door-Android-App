package com.atyourdoorteam.atyourdoor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.entities.GroceryList;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CheckOutItemsListAdapter extends RecyclerView.Adapter<CheckOutItemsListAdapter.CheckOutItemsViewHolder> {

    private ArrayList<Cart> cartArrayList;
    private Context context;

    public CheckOutItemsListAdapter( Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CheckOutItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_list_checkout_items, parent, false);
        CheckOutItemsViewHolder checkOutItemsViewHolder = new CheckOutItemsViewHolder(listItem);
        return checkOutItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutItemsViewHolder holder, int position) {
        final Cart cart = cartArrayList.get(position);

        Glide.with(context).load(cart.getProductImageURL()).into(holder.productImage);
        holder.productName.setText(cart.getProductName());
        holder.productPrice.setText(String.valueOf(cart.getProductPrice()));
        holder.productQuantity.setText(String.valueOf(cart.getProductQuantity()));

    }

    @Override
    public int getItemCount() {
        if (cartArrayList != null) {
            return cartArrayList.size();
        } else {
        }
        return 0;
    }

    public void setLists(ArrayList<Cart> cartList) {
        this.cartArrayList = cartList;
        notifyDataSetChanged();
    }

    public static class CheckOutItemsViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName, productQuantity, productPrice;

        public CheckOutItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
        }
    }
}
