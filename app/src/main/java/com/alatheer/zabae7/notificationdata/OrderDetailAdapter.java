package com.alatheer.zabae7.notificationdata;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHolder> {
    List<OrderDetail> orderDetailList;
    Context context;

    public OrderDetailAdapter(List<OrderDetail> orderDetailList, Context context) {
        this.orderDetailList = orderDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_detail_item,parent,false);
        return new OrderDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailHolder holder, int position) {
        holder.setData(orderDetailList.get(position));
        holder.eye_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createpopup(orderDetailList.get(position));
            }
        });
    }

    private void createpopup(OrderDetail orderDetail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.order_details_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        TextView txt_size_name = view.findViewById(R.id.txt_size_name);
        TextView txt_cutting_title = view.findViewById(R.id.txt_cutting_name);
        TextView txt_cutting_head_title = view.findViewById(R.id.txt_cutting_head_name);
        TextView txt_package_name = view.findViewById(R.id.txt_package_name);
        TextView txt_size_price = view.findViewById(R.id.txt_size_price);
        TextView txt_cutting_price = view.findViewById(R.id.txt_cutting_price);
        TextView txt_cutting_head_price = view.findViewById(R.id.txt_cutting_head_price);
        TextView txt_package_price = view.findViewById(R.id.txt_package_price);
        txt_size_name.setText(orderDetail.getSizeName());
        txt_size_price.setText(orderDetail.getSizePrice());
        txt_cutting_title.setText(orderDetail.getCuttingTitle());
        txt_cutting_price.setText(orderDetail.getCuttingPrice());
        txt_cutting_head_title.setText(orderDetail.getHeadCuttingTitle());
        txt_cutting_head_price.setText(orderDetail.getCuttingHeadPrice());
        txt_package_name.setText(orderDetail.getPackageTitle());
        txt_package_price.setText(orderDetail.getPackagPrice());
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    class OrderDetailHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_product_name)
        TextView txt_product_name;
        @BindView(R.id.txt_product_price)
        TextView txt_product_price;
        @BindView(R.id.txt_product_amount)
        TextView txt_product_amount;
        @BindView(R.id.product_img)
        ImageView product_img;
        @BindView(R.id.eye_img)
        ImageView eye_img;
        @BindView(R.id.txt_product_size)
        TextView txt_product_size;
        public OrderDetailHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(OrderDetail orderDetail) {
            txt_product_name.setText(orderDetail.getProductName());
            txt_product_amount.setText(orderDetail.getProductQty());
            txt_product_size.setText(orderDetail.getSizeName());
            txt_product_price.setText(Double.parseDouble(orderDetail.getProductQty())*Double.parseDouble(orderDetail.getSizePrice())+"");
            Picasso.get().load("https://thabaeh.com/uploads/product/"+orderDetail.getProductImage()).into(product_img);
        }
    }
}
