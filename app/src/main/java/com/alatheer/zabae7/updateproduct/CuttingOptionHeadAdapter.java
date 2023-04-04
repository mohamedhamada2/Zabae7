package com.alatheer.zabae7.updateproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.home.home2.CuttingOptionHead;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CuttingOptionHeadAdapter extends RecyclerView.Adapter<CuttingOptionHeadAdapter.CuttingOptionHeadHolder> {
    List<CuttingOptionHead> cuttingOptionHeadList;
    int checkedPosition = -1;
    Context context;
    UpdateProductFragment updateProductFragment;
    String cutting_id;
    public CuttingOptionHeadAdapter(List<CuttingOptionHead> cuttingOptionHeadList, Context context,UpdateProductFragment updateProductFragment,String cutting_id) {
        this.cuttingOptionHeadList = cuttingOptionHeadList;
        this.context = context;
        this.updateProductFragment = updateProductFragment;
        this.cutting_id = cutting_id;
    }

    @NonNull
    @Override
    public CuttingOptionHeadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.size_item,parent,false);
        return new CuttingOptionHeadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuttingOptionHeadHolder holder, int position) {
        holder.setData(cuttingOptionHeadList.get(position));
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedPosition != holder.getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = holder.getAdapterPosition();
                    updateProductFragment.send_cutting_head(cuttingOptionHeadList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cuttingOptionHeadList.size();
    }

    class CuttingOptionHeadHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        RadioButton checkbox;
        @BindView(R.id.size_name)
        TextView size_name;
        public CuttingOptionHeadHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(CuttingOptionHead cuttingOptionHead) {
            if (cuttingOptionHead.getId().equals(cutting_id)){
                checkbox.setChecked(true);
            }else {
                if (checkedPosition == -1) {
                    checkbox.setChecked(false);
                } else {
                    if (checkedPosition == getAdapterPosition()) {
                        checkbox.setChecked(true);
                    } else {
                        checkbox.setChecked(false);
                    }
                }
            }
            size_name.setText(cuttingOptionHead.getTitle());
        }
    }
}
