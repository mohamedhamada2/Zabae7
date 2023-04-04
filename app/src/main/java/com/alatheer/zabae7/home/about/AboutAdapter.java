package com.alatheer.zabae7.home.about;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.zabae7.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.AboutHolder> {
    List<AppInfo> appInfoList;
    Context context;

    public AboutAdapter(List<AppInfo> appInfoList, Context context) {
        this.appInfoList = appInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public AboutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.about_item,parent,false);
        return new AboutHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutHolder holder, int position) {
        holder.setData(appInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }

    class AboutHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.description)
        TextView description;
        public AboutHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(AppInfo appInfo) {
            logo.setImageResource(R.drawable.logo);
            name.setText(appInfo.getNameweb());
            description.setText(Html.fromHtml(appInfo.getAboutApp()));
        }
    }
}
