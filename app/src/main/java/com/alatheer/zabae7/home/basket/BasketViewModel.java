package com.alatheer.zabae7.home.basket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.home.profile.UserModel;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;
import com.alatheer.zabae7.singleton.OrderItemsSingleTone;

import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketViewModel {
    Context context;
    BasketFragment basketFragment;
    UserModel userModel;
    OrderItemsSingleTone orderItemsSingleTone;
    DatabaseClass databaseClass;
    String payment_method;

    public BasketViewModel(Context context, BasketFragment basketFragment) {
        this.context = context;
        this.basketFragment = basketFragment;
        orderItemsSingleTone = OrderItemsSingleTone.newInstance();
        databaseClass = Room.databaseBuilder(context.getApplicationContext(),DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
    }

    public void getProfileData(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<UserModel> call = getDataService.get_profile(user_id);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()){
                        userModel = response.body();
                        basketFragment.setData(userModel);
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });
        }
    }

    public void sendOrder(BasketModel basketModel) {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<OrderSuccess> call = getDataService.send_order(basketModel);
        call.enqueue(new Callback<OrderSuccess>() {
            @Override
            public void onResponse(Call<OrderSuccess> call, Response<OrderSuccess> response) {
                if(response.isSuccessful()){
                    if(response.body().getSuccess()==1);
                    Toast.makeText(context,"تم استلام الطلب وسيتم التواصل معك لاستكمال البيانات ...", Toast.LENGTH_LONG).show();
                    databaseClass.getDao().deleteAllproduct();
                    basketFragment.go_to_home();
                }
            }

            @Override
            public void onFailure(Call<OrderSuccess> call, Throwable t) {

            }
        });
    }

    public void CreateDialog(BasketModel basketModel) {
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(context).inflate(R.layout.payment_dialog, null);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        RadioGroup radioGroup = view.findViewById(R.id.radio);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Checked_id = radioGroup.getCheckedRadioButtonId();
                if (Checked_id == -1){
                    Toast.makeText(context, "اختر طرية الدفع", Toast.LENGTH_SHORT).show();
                }else {
                    findRadioButton(Checked_id,basketModel);
                    dialog.dismiss();

                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //dialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        try {
            dialog.show();
        }catch (WindowManager.BadTokenException e){
            dialog.dismiss();
        }
    }

    private void findRadioButton(int checked_id, BasketModel basketModel) {
        switch (checked_id){
            case R.id.radio_btn1:
                payment_method ="1";
                basketModel.setPay_method(payment_method);
                sendOrder(basketModel);
                break;
            case R.id.radio_btn2:
                payment_method="2";
                basketModel.setPay_method(payment_method);
                sendOrder(basketModel);
                break;
        }
    }

    public void getNotifications(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<NotificationModel> call = getDataService.get_notifications(user_id);
            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    if (response.isSuccessful()){
                        basketFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }
}
