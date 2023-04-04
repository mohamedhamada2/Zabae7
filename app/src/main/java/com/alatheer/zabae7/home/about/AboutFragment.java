package com.alatheer.zabae7.home.about;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.databinding.FragmentTermsBinding;
import com.alatheer.zabae7.home.profile.MainProfileFragment;
import com.alatheer.zabae7.home.profile.ProfileFragment;

import java.util.List;

public class AboutFragment extends Fragment {
    FragmentTermsBinding fragmentTermsBinding;
    AboutViewModel aboutViewModel;
    AboutAdapter aboutAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTermsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_terms, container, false);
        View view = fragmentTermsBinding.getRoot();
        aboutViewModel = new AboutViewModel(getActivity(),this);
        fragmentTermsBinding.setAboutviewmodel(aboutViewModel);
        fragmentTermsBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getFragmentManager().popBackStack();
            }
        });
        aboutViewModel.getAbout();
        aboutViewModel.get_user_data();
        return view;
    }


    public void initrecycler(List<AppInfo> appInfo) {
        fragmentTermsBinding.aboutRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentTermsBinding.aboutRecycler.setLayoutManager(layoutManager);
        aboutAdapter = new AboutAdapter(appInfo,getActivity());
        fragmentTermsBinding.aboutRecycler.setAdapter(aboutAdapter);
    }

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentTermsBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentTermsBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentTermsBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }
}
