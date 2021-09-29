package com.atyourdoorteam.atyourdoor.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Activities.ProductsActivity;
import com.atyourdoorteam.atyourdoor.models.SubCategories;

import java.util.List;

public class ShopSubCategoriesAdapter extends RecyclerView.Adapter<ShopSubCategoriesAdapter.ShopSubViewHolder> {

    private List<SubCategories> subCategoriesList;
    private String shopId;


    public ShopSubCategoriesAdapter(List<SubCategories> subCategoriesList, String shopId) {
        this.subCategoriesList = subCategoriesList;
        this.shopId = shopId;
    }

    @NonNull
    @Override
    public ShopSubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_subcategories_detail, parent, false);
        ShopSubViewHolder shopSubViewHolder = new ShopSubViewHolder(listItem);
        return shopSubViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopSubViewHolder holder, int position) {
        final SubCategories subCategories = subCategoriesList.get(position);

        holder.subCategoryName.setText(subCategories.getSubCategoryName());
        holder.subCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToProductsActivity = new Intent(v.getContext(), ProductsActivity.class);
                navigateToProductsActivity.putExtra("SHOPID", shopId);
                navigateToProductsActivity.putExtra("SUBCATEGORYID", subCategories.getId());
                v.getContext().startActivity(navigateToProductsActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoriesList.size();
    }

    public static class ShopSubViewHolder extends RecyclerView.ViewHolder {

        public TextView subCategoryName;
        public ShopSubViewHolder(@NonNull View itemView) {
            super(itemView);

            subCategoryName = itemView.findViewById(R.id.subCategoryName);
        }
    }
}
