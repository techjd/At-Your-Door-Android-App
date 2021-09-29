package com.atyourdoorteam.atyourdoor.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.models.FinalOrderedItems.FinalOrders;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private ArrayList<FinalOrders> orders;

    public OrdersAdapter(ArrayList<FinalOrders> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_orders_list, parent, false);
        OrderViewHolder orderViewHolder = new OrderViewHolder(listItem);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        final FinalOrders finalOrders = orders.get(position);
        holder.shopName.setText(finalOrders.getShopId().getShopName());
        holder.totalPrice.setText(String.valueOf(finalOrders.getTotalPrice()));
        holder.isPaid.setText(finalOrders.getIsPaid().toString());
        holder.modeOFPayment.setText(finalOrders.getOrderMode());
        holder.deliveryAddress.setText(finalOrders.getShippingAddress());

        for (int i = 0; i < finalOrders.getOrderedItems().size(); i++) {
            String content = "";

            content += finalOrders.getOrderedItems().get(i).getProductName() + "  (" + finalOrders.getOrderedItems().get(i).getProductQuantity().toString() + ")" + "\n";

            holder.orderedItems.append(content);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String date = finalOrders.getCreatedAt();
        try {
            Date finalDate = dateFormat.parse(date);
            Log.d("WHATS DATE", finalDate.toString());
            holder.orderedDate.setText(finalDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            holder.orderedDate.setText(finalOrders.getCreatedAt());
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView shopName, totalPrice, isPaid, modeOFPayment, orderedItems, deliveryAddress, orderedDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.orderedShopName);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            isPaid = itemView.findViewById(R.id.isPaid);
            modeOFPayment = itemView.findViewById(R.id.modeOFPayment);
            orderedItems = itemView.findViewById(R.id.orderedItems);
            deliveryAddress = itemView.findViewById(R.id.deliveryAddress);
            orderedDate = itemView.findViewById(R.id.orderedDate);
        }
    }
}
