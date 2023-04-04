package com.alatheer.zabae7.home.orders;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alatheer.zabae7.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.StoreDetailsHolder> {
    List<OrderStatus> storeDetailsList;
    Context context,m_context;
    Integer selectedItem = 0;
    OrdersFragment pointsFragment;
    Resources resources;

    public OrderStatusAdapter(List<OrderStatus> storeDetailsList, Context context, OrdersFragment pointsFragment) {
        this.storeDetailsList = storeDetailsList;
        this.context = context;
        this.pointsFragment = pointsFragment;
    }

    @NonNull
    @Override
    public StoreDetailsHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_status_item,parent,false);
        return new StoreDetailsHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull StoreDetailsHolder holder, int position) {
        holder.setData(storeDetailsList.get(position));
        holder.linearLayout.setBackgroundResource(R.drawable.txt_non_active_bg);
        holder.txt_title.setTextColor(context.getColor(R.color.lightsilver));
        holder.img.setColorFilter(context.getResources().getColor(R.color.lightsilver));
        if (selectedItem == position) {
            holder.linearLayout.setBackgroundResource(R.drawable.txt_active_bg);
            holder.txt_title.setTextColor(context.getColor(R.color.colorPrimaryDark));
            holder.img.setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousItem);
                notifyItemChanged(position);
                if (position == 0){
                    pointsFragment.getallorders("1");
                }else if (position == 1){
                    pointsFragment.getallorders("2");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeDetailsList.size();
    }

    class StoreDetailsHolder extends RecyclerView.ViewHolder{
        TextView txt_title;
        ConstraintLayout constraintLayout;
        ImageView img;
        LinearLayout linearLayout;
        public StoreDetailsHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_order_status);
            linearLayout = itemView.findViewById(R.id.linear2);
            img = itemView.findViewById(R.id.img);
        }

        public void setData(OrderStatus orderStatus) {
            txt_title.setText(orderStatus.getAr_name());
            img.setImageResource(orderStatus.getImg());
        }
    }

}
