package com.atyourdoorteam.atyourdoor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.db.entities.GroceryList;

import java.util.ArrayList;

public class GroceriesListAdapter extends RecyclerView.Adapter<GroceriesListAdapter.GroceriesListViewHolder> {

    private ArrayList<GroceryList> groceryLists;

    @NonNull
    @Override
    public GroceriesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_list_layout, parent, false);
        GroceriesListViewHolder groceriesListViewHolder = new GroceriesListViewHolder(listItem);
        return groceriesListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroceriesListViewHolder holder, int position) {
        final GroceryList groceryList = groceryLists.get(position);

        holder.itemName.setText(groceryList.getProductName());

    }

    @Override
    public int getItemCount() {
        if (groceryLists != null) {
            return groceryLists.size();
        } else {
        }
        return 0;
    }

    public void setGroceryLists(ArrayList<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
        notifyDataSetChanged();
    }

    public static class GroceriesListViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public ImageView edit;

        public GroceriesListViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.groceryItem);
            edit = itemView.findViewById(R.id.editItem);
        }
    }
}
