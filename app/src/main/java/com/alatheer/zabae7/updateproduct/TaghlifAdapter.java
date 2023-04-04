package com.alatheer.zabae7.updateproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.home.home2.TaghlifOption;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaghlifAdapter extends RecyclerView.Adapter<TaghlifAdapter.TaghlifHolder> {
    List<TaghlifOption> taghlifOptionList;
    Context context;
    int checkedPosition = -1 ;
    UpdateProductFragment updateProductFragment;
    public TaghlifAdapter(List<TaghlifOption> taghlifOptionList, Context context,UpdateProductFragment updateProductFragment) {
        this.taghlifOptionList = taghlifOptionList;
        this.context = context;
        this.updateProductFragment = updateProductFragment;
    }

    @NonNull
    @Override
    public TaghlifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.size_item,parent,false);
        return new TaghlifHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaghlifHolder holder, int position) {
        holder.setData(taghlifOptionList.get(position));
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedPosition != holder.getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    updateProductFragment.send_package(taghlifOptionList.get(position));
                    checkedPosition = holder.getAdapterPosition();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taghlifOptionList.size();
    }

    class TaghlifHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        RadioButton checkbox;
        @BindView(R.id.size_name)
        TextView size_name;
        public TaghlifHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(TaghlifOption taghlifOption) {
            if (checkedPosition == -1) {
                checkbox.setChecked(false);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
            }
            size_name.setText(taghlifOption.getTitle());
        }
    }
}
