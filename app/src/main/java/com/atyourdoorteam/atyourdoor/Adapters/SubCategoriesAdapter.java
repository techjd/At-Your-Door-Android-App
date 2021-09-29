package com.atyourdoorteam.atyourdoor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Activities.SubCategoryProductActivity;
import com.atyourdoorteam.atyourdoor.models.SubCategories;

import java.util.List;

public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.SubCategoriesViewHolder> {

    private List<SubCategories> subCategoriesList;
    private Context context;

    public SubCategoriesAdapter(List<SubCategories> subCategoriesList, Context context) {
        this.subCategoriesList = subCategoriesList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_subcategories_detail, parent, false);
        SubCategoriesViewHolder subCategoriesViewHolder = new SubCategoriesViewHolder(listItem);
        return subCategoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesViewHolder holder, int position) {
        final SubCategories subCategories = subCategoriesList.get(position);

        holder.subCategoryName.setText(subCategories.getSubCategoryName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToSpecificSubCategory = new Intent(context, SubCategoryProductActivity.class);
                navigateToSpecificSubCategory.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                navigateToSpecificSubCategory.putExtra("SUBCATEGORYID", subCategories.getId());
                context.startActivity(navigateToSpecificSubCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoriesList.size();
    }

    public static class SubCategoriesViewHolder extends RecyclerView.ViewHolder {
        public TextView subCategoryName;
        public CardView cardView;

        public SubCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            subCategoryName = itemView.findViewById(R.id.subCategoryName);
            cardView = itemView.findViewById(R.id.carViewOfSubCategory);
        }
    }
}
