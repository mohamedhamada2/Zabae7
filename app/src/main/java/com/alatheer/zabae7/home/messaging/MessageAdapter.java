package com.alatheer.zabae7.home.messaging;

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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    List<MessageModel> messageModelList;
    Context context;
    MessagingFragment messagingFragment;

    public MessageAdapter(List<MessageModel> messageModelList, Context context,MessagingFragment messagingFragment) {
        this.messageModelList = messageModelList;
        this.context = context;
        this.messagingFragment = messagingFragment;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.setData(messageModelList.get(position));
        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagingFragment.setData(messageModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_message_num)
        TextView txt_message_num;
        @BindView(R.id.txt_message_title)
        TextView txt_message_title;
        @BindView(R.id.txt_message_date)
        TextView txt_message_date;
        @BindView(R.id.btn_confirm)
        TextView btn_confirm;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(MessageModel messageModel) {
            txt_message_num.setText(messageModel.getMsgId());
            txt_message_title.setText(messageModel.getMsgTitle());
            txt_message_date.setText(messageModel.getMsgDate());
        }
    }
}
