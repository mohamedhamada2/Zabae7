package com.alatheer.zabae7.updateproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.home.home2.ProductSize;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductSizeAdapter extends RecyclerView.Adapter<ProductSizeAdapter.ProductSizeHolder> {
    Context context;
    List<ProductSize> sizeModelList;
    int checkedPosition = -1;
    UpdateProductFragment updateProductFragment;
    public ProductSizeAdapter(Context context, List<ProductSize> sizeModelList,UpdateProductFragment updateProductFragment) {
        this.context = context;
        this.sizeModelList = sizeModelList;
        this.updateProductFragment = updateProductFragment;
    }

    @NonNull
    @Override
    public ProductSizeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.size_item,parent,false);
        return new ProductSizeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSizeHolder holder, int position) {
        holder.setData(sizeModelList.get(position));
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedPosition != holder.getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = holder.getAdapterPosition();
                    updateProductFragment.send_size(sizeModelList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sizeModelList.size();
    }

    class ProductSizeHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        RadioButton checkbox;
        @BindView(R.id.size_name)
        TextView size_name;
        public ProductSizeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(ProductSize productSize) {
            if (checkedPosition == -1) {
                checkbox.setChecked(false);
                size_name.setTextColor(context.getResources().getColor(R.color.dark_silver));
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    checkbox.setChecked(true);
                    size_name.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    checkbox.setChecked(false);
                    size_name.setTextColor(context.getResources().getColor(R.color.dark_silver));
                }
            }
            size_name.setText(productSize.getSizeName());
        }
    }
}
