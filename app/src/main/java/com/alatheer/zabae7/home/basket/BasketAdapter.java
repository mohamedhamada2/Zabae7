package com.alatheer.zabae7.home.basket;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.Dao;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.singleton.OrderItemsSingleTone;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketHolder> {
    Context context;
    List<OrderItemList> orderItemListList;
    BasketFragment basketFragment;
    OrderItemsSingleTone orderItemsSingleTone;
    DatabaseClass databaseClass;
    int count;
    public BasketAdapter(Context context, List<OrderItemList> orderItemListList,BasketFragment basketFragment) {
        this.context = context;
        this.orderItemListList = orderItemListList;
        this.basketFragment = basketFragment;
        databaseClass = Room.databaseBuilder(context.getApplicationContext(),DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public BasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.basket_item,parent,false);
        return new BasketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketHolder holder, int position) {
        holder.setData(orderItemListList.get(position));
        holder.add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderItemList orderDetails = orderItemListList.get(position);
                count = Integer.parseInt(holder.txt_product_amount.getText().toString())+1;
                int package_price = Integer.parseInt(orderItemListList.get(position).getPackag_price());
                int cutting_price = Integer.parseInt(orderItemListList.get(position).getCutting_price());
                int cutting__head_price = Integer.parseInt(orderItemListList.get(position).getCutting_head_price());
                int addition_price = package_price+cutting_price+cutting__head_price;
                int price = Integer.parseInt(orderItemListList.get(position).getSize_price())* count;
                int total_price = price+addition_price;
                orderDetails.setTotal_price(total_price+"");
                holder.txt_product_amount.setText(count+"");
                holder.txt_product_price.setText(total_price+"");
                orderDetails.setProduct_qty(count+"");
                databaseClass.getDao().editproduct(orderDetails);
                basketFragment.UpdateList();
            }
        });
        holder.minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderItemList orderDetails = orderItemListList.get(position);
                if (Integer.parseInt(holder.txt_product_amount.getText().toString())==1){
                    count = 1;
                }else {
                    count = Integer.parseInt(holder.txt_product_amount.getText().toString())-1;
                }
                holder.txt_product_amount.setText(count+"");
                int package_price = Integer.parseInt(orderItemListList.get(position).getPackag_price());
                int cutting_price = Integer.parseInt(orderItemListList.get(position).getCutting_price());
                int cutting_head = Integer.parseInt(orderItemListList.get(position).getCutting_head_price());
                int addition_price = package_price+cutting_price+cutting_head;
                int price = Integer.parseInt(orderItemListList.get(position).getSize_price())* count;
                int total_price = price+addition_price;
                orderDetails.setTotal_price(total_price+"");
                holder.txt_product_price.setText(total_price+"");
                orderDetails.setProduct_qty(count+"");
                databaseClass.getDao().editproduct(orderDetails);
                basketFragment.UpdateList();
            }
        });
        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_delete_dialog(orderItemListList.get(position));
                //orderItemsSingleTone.RemoveProduct(orderItemListList.get(position));
            }
        });
        holder.edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basketFragment.update_order(orderItemListList.get(position));
                //orderItemsSingleTone.RemoveProduct(orderItemListList.get(position));
            }
        });
    }

    private void create_delete_dialog(OrderItemList orderItemList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.delete_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        Button skip_btn = view.findViewById(R.id.btn_skip);
        Button delete_btn = view.findViewById(R.id.btn_delete);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseClass.getDao().DeleteProduct(orderItemList.getProduct_id(),Integer.parseInt(orderItemList.getSize_id()));
                basketFragment.UpdateList();
                basketFragment.restart();
                dialog3.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItemListList.size();
    }

    class BasketHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_size_name)
        TextView txt_size_name;
        @BindView(R.id.txt_product_amount)
        TextView txt_product_amount;
        @BindView(R.id.product_img)
        ImageView  product_img;
        @BindView(R.id.img_add)
        ImageView add_img;
        @BindView(R.id.img_minus)
        ImageView minus_img;
        @BindView(R.id.txt_product_price)
        TextView txt_product_price;
        @BindView(R.id.txt_product_name)
        TextView txt_product_name;
        @BindView(R.id.delete_img)
        ImageView delete_img;
        @BindView(R.id.edit_img)
        ImageView edit_img;
        public BasketHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(OrderItemList orderItemList) {
            txt_product_amount.setText(orderItemList.getProduct_qty());
            txt_size_name.setText(orderItemList.getSize_name());
            txt_product_name.setText(orderItemList.getProduct_name());
            /*if (orderItemList.getCutting_id().equals("0")){
                txt_cutting.setVisibility(View.GONE);
            }else {
                txt_cutting.setText(orderItemList.getCutting_name());
            }
            if (orderItemList.getPackag_id().equals("0")){
                txt_package_name.setVisibility(View.GONE);
            }else {
                txt_package_name.setText(orderItemList.getPackag_name());
            }*/
            //txt_product_price.setText(orderItemList.getProduct_price()+"");
            Picasso.get().load(orderItemList.getProduct_img()).into(product_img);
            txt_product_price.setText(orderItemList.getTotal_price());
        }
    }
}
