package com.alatheer.zabae7.home.messaging;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.databinding.ActivityPaymentDataBinding;
import com.alatheer.zabae7.databinding.FragmentMessageDetailsBinding;


public class MessageDetailsFragment extends Fragment {
    FragmentMessageDetailsBinding fragmentMessageDetailsBinding;
    MessageModel messageModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMessageDetailsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_message_details, container, false);
        View view = fragmentMessageDetailsBinding.getRoot();
        messageModel = (MessageModel) getArguments().getSerializable("message");
        fragmentMessageDetailsBinding.txtMessageNum.setText(messageModel.getMsgId());
        fragmentMessageDetailsBinding.txtMessageTitle.setText(messageModel.getMsgTitle());
        fragmentMessageDetailsBinding.txtMessageDate.setText(messageModel.getMsgDate());
        fragmentMessageDetailsBinding.txtContent.setText(messageModel.getMsgContent());
        return  view;
    }
}