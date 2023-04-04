package com.alatheer.zabae7.home.home2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CuttingOptionAdapter extends RecyclerView.Adapter<CuttingOptionAdapter.CuttingOptionHolder> {
    List<CuttingOption> cuttingOptionList;
    Context context;
    int checkedPosition = -1;
    ProductDetailsFragment productDetailsFragment;
    public CuttingOptionAdapter(List<CuttingOption> cuttingOptionList, Context context,ProductDetailsFragment productDetailsFragment) {
        this.cuttingOptionList = cuttingOptionList;
        this.context = context;
        this.productDetailsFragment = productDetailsFragment;
    }

    @NonNull
    @Override
    public CuttingOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.size_item,parent,false);
        return new CuttingOptionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuttingOptionHolder holder, int position) {
        holder.setData(cuttingOptionList.get(position));
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedPosition != holder.getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = holder.getAdapterPosition();
                }
                if (holder.checkbox.isChecked()){
                    productDetailsFragment.send_cutting(cuttingOptionList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cuttingOptionList.size();
    }

    class CuttingOptionHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        RadioButton checkbox;
        @BindView(R.id.size_name)
        TextView size_name;

        public CuttingOptionHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(CuttingOption cuttingOption) {
            if (checkedPosition == -1) {
                checkbox.setChecked(false);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
            }
            size_name.setText(cuttingOption.getTitle());
        }
    }
}
