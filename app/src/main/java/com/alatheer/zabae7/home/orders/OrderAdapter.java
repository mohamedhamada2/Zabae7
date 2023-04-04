package com.alatheer.zabae7.home.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alatheer.zabae7.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    List<Order> orderList;
    Context context;
    OrdersFragment ordersFragment;
    public OrderAdapter(List<Order> orderList, Context context,OrdersFragment ordersFragment) {
        this.orderList = orderList;
        this.context = context;
        this.ordersFragment = ordersFragment;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        holder.setData(orderList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersFragment.sendData(orderList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_order_num)
        TextView txt_order_num;
        @BindView(R.id.txt_order_status)
        TextView txt_order_status;
        @BindView(R.id.txt_order_date)
        TextView txt_order_date;
        @BindView(R.id.txt_order_total_price)
        TextView txt_order_total_price;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Order order) {
            txt_order_num.setText(order.getOrderId()+"");
            txt_order_status.setText(order.getHaletTalab());
            txt_order_date.setText(order.getOrderDate());
            if (order.getPromo_value() != null){
            if (order.getPromo_value().equals("0")){
                txt_order_total_price.setText(order.getAllSum()+"");
            }else {
                Integer total_price = order.getAllSum()-Integer.parseInt(order.getPromo_value());
                txt_order_total_price.setText(total_price+"");
            }
        }else {
            txt_order_total_price.setText(order.getAllSum()+"");
        }
            if (order.getHaletTalab().equals("neworder")){
                txt_order_status.setTextColor(context.getResources().getColor(R.color.neworder));
                txt_order_status.setBackgroundResource(R.drawable.status_waiting_bg);
                txt_order_status.setText("طلب جديد");
            }else if (order.getHaletTalab().equals("inprogress")) {
                txt_order_status.setTextColor(context.getResources().getColor(R.color.inprogress));
                txt_order_status.setBackgroundResource(R.drawable.status_inprogress_bg);
                txt_order_status.setText("قيد التنفيذ");
            }else if (order.getHaletTalab().equals("tawsel")){
                txt_order_status.setTextColor(context.getResources().getColor(R.color.inprogress));
                txt_order_status.setBackgroundResource(R.drawable.status_inprogress_bg);
                txt_order_status.setText("قيد التوصيل");
            }else if (order.getHaletTalab().equals("cancelled")){
                txt_order_status.setTextColor(context.getResources().getColor(R.color.cancelled));
                txt_order_status.setText("ملغي");
                txt_order_status.setBackgroundResource(R.drawable.status_cancelled_bg);
            }else if (order.getHaletTalab().equals("completed")) {
                txt_order_status.setBackgroundResource(R.drawable.status_deliverd_bg);
                txt_order_status.setTextColor(context.getResources().getColor(R.color.completed));
                txt_order_status.setText("تم تستليمه");
            }
        }
    }
}
