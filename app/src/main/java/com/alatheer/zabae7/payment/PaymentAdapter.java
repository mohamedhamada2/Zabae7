package com.alatheer.zabae7.payment;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.home.home2.ProductSize;
import com.alatheer.zabae7.home.home2.ProductSizeAdapter;
import com.alatheer.zabae7.home.orders.OrderStatus;
import com.alatheer.zabae7.home.orders.OrdersFragment;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ProductSizeHolder> {
    List<Payment> paymentList;
    Context context,m_context;
    Integer selectedItem = 0;
    PaymentDataActivity paymentDataActivity;
    Resources resources;
    public PaymentAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
        paymentDataActivity = (PaymentDataActivity) context;
    }

    @NonNull
    @Override
    public PaymentAdapter.ProductSizeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_item,parent,false);
        return new PaymentAdapter.ProductSizeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ProductSizeHolder holder, int position) {
        holder.setData(paymentList.get(position));
        holder.constraintLayout.setBackgroundResource(R.drawable.txt_login_background);
        holder.payment_method.setTextColor(context.getColor(R.color.dark_silver));
        holder.checkbox.setBackgroundResource(R.drawable.ic_unchecked);
        if (selectedItem == position) {
            holder.constraintLayout.setBackgroundResource(R.drawable.txt_payment_bg);
            holder.payment_method.setTextColor(context.getColor(R.color.white));
            holder.checkbox.setBackgroundResource(R.drawable.ic_checked2);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousItem);
                notifyItemChanged(position);
                paymentDataActivity.setData(paymentList.get(position));
                /*if (position == 0){
                    pointsFragment.getallorders("2");
                }else if (position == 1){
                    pointsFragment.getallorders("1");
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    class ProductSizeHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        ImageView checkbox;
        @BindView(R.id.payment_method)
        TextView payment_method;
        @BindView(R.id.layout)
        ConstraintLayout constraintLayout;
        public ProductSizeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(Payment productSize) {
            payment_method.setText(productSize.getName());
        }
    }
}
