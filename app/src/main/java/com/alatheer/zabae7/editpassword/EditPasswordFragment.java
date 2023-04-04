package com.alatheer.zabae7.editpassword;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentEditPasswordBinding;
import com.alatheer.zabae7.home.profile.UserModel;
import com.alatheer.zabae7.login.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class EditPasswordFragment extends Fragment {
    FragmentEditPasswordBinding fragmentEditPasswordBinding;
    EditPasswordViewModel editPasswordViewModel;
    String old_password,new_password,confirm_password;
    UserSharedPreference userSharedPreference;
    User userModel;
    String user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentEditPasswordBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_edit_password, container, false);
        editPasswordViewModel = new EditPasswordViewModel(getActivity(),this);
        View view = fragmentEditPasswordBinding.getRoot();
        userSharedPreference = UserSharedPreference.getInstance();
        userModel = userSharedPreference.Get_UserData(getActivity());
        user_id = userModel.getMember().getUserId();
        editPasswordViewModel.getNotifications(user_id);
        BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
        BottomNavigationView navBar2 = getActivity().findViewById(R.id.home_bottomnavigation2);
        FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 0);
        fragment_container.setLayoutParams(params);
        navBar.setVisibility(View.GONE);
        fragmentEditPasswordBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        fragmentEditPasswordBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        return view;
    }

    private void validation() {
        old_password = fragmentEditPasswordBinding.etOldPassword.getText().toString();
        new_password = fragmentEditPasswordBinding.etNewPassword.getText().toString();
        confirm_password = fragmentEditPasswordBinding.etConfirmPassword.getText().toString();
        if (!TextUtils.isEmpty(old_password)&&!TextUtils.isEmpty(new_password)&&!TextUtils.isEmpty(confirm_password)
        &&confirm_password.equals(new_password)){
            editPasswordViewModel.edit_password(user_id,old_password,new_password);
        }else {
            if (TextUtils.isEmpty(old_password)){
                fragmentEditPasswordBinding.etOldPassword.setError("أدخل كلمة المرور القديمة");
            }else {
                fragmentEditPasswordBinding.etOldPassword.setError(null);
            }
            if (TextUtils.isEmpty(new_password)){
                fragmentEditPasswordBinding.etNewPassword.setError("ادخل كلمة المرور الجديدة");
            }else {
                fragmentEditPasswordBinding.etNewPassword.setError(null);
            }
            if (TextUtils.isEmpty(confirm_password)){
                fragmentEditPasswordBinding.etConfirmPassword.setError("كلمة المرور غير متطابقة");
            }else {
                fragmentEditPasswordBinding.etConfirmPassword.setError(null);
            }
        }
    }

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentEditPasswordBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentEditPasswordBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentEditPasswordBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }
}