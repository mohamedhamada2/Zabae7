package com.alatheer.zabae7.notifications;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.notificationdata.NotificationsDataActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    List<Record> recordList;
    Context context;
    NotificationsFragment notificationsFragment;
    HomeActivity homeActivity;

    public NotificationAdapter(List<Record> recordList,Context context,NotificationsFragment notificationsFragment) {
        this.recordList = recordList;
        this.notificationsFragment = notificationsFragment;
        this.context = context;
        homeActivity = (HomeActivity) context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notifications_item,parent,false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  NotificationAdapter.NotificationHolder holder, int position) {
        if (recordList.get(position)!= recordList.get(0)){
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.setData(recordList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recordList.get(position).getType().equals("order")){
                    Intent intent = new Intent(context,NotificationsDataActivity.class);
                    intent.putExtra("id",recordList.get(position).getOrder_id());
                    intent.putExtra("flag",1);
                    context.startActivity(intent);
                }if (recordList.get(position).getType().equals("offer")){
                    //Toast.makeText(context, recordList.get(position).getOrder_id(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,HomeActivity.class);
                    intent.putExtra("moredata",recordList.get(position).getOrder_id());
                    intent.putExtra("flag",2);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class NotificationHolder extends RecyclerView.ViewHolder{
        TextView txt_title,txt_description,txt_date;
        ImageView img;
        RelativeLayout relativeLayout;
        public NotificationHolder(@NonNull  View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_description = itemView.findViewById(R.id.txt_description);
            txt_date = itemView.findViewById(R.id.txt_date);
            img = itemView.findViewById(R.id.img);
            relativeLayout = itemView.findViewById(R.id.relative5);
        }

        public void setData(Record record) {
            txt_title.setText(record.getTitle());
            txt_description.setText(record.getBody());
            txt_date.setText(record.getDate());
            if (record.getType().equals("order")){
                if (record.getStatus().equals("inprogress")){
                    img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_834));
                }else if (record.getStatus().equals("completed")){
                    img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_834__1_));
                }else if (record.getStatus().equals("tawsel")){
                    img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_834__3_));
                }else if (record.getStatus().equals("cancelled")){
                    img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_834__2_));
                }
            }else {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_834__4_));
            }
        }
    }
}
