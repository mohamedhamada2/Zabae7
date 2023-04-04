package com.alatheer.zabae7.notificationdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.databinding.ActivityNotificationsDataBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationsDataActivity extends AppCompatActivity {
    ActivityNotificationsDataBinding activityNotificationsDataBinding;
    NotificationDataViewModel notificationDataViewModel;
    ProgressDialog pd;
    public static NotificationsDataActivity pa;
    String title,message,data;
    OrderDetailAdapter orderDetailAdapter;
    RecyclerView.LayoutManager layoutManager;
    Integer flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_data);
        activityNotificationsDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_notifications_data);
        notificationDataViewModel = new NotificationDataViewModel(this);
        activityNotificationsDataBinding.setNotificationDataViewModel(notificationDataViewModel);
        activityNotificationsDataBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        pd = new ProgressDialog(NotificationsDataActivity.this);
        pd.setMessage("تحميل ...");
        pd.show();
        pa = this;
        try {
            getDataIntent();
        } catch (JSONException e) {
            Log.e("id_error",e.getMessage());
            e.printStackTrace();
        }
    }

    private void getDataIntent() throws JSONException {
        try {
            title = getIntent().getStringExtra("title");
            message = getIntent().getStringExtra("message");
            data = getIntent().getStringExtra("moredata");
            JSONObject json = new JSONObject(data);
            String id = (String) json.get("id");
            notificationDataViewModel.get_order(id);
        }catch (Exception e) {
            String id = getIntent().getStringExtra("id");
            notificationDataViewModel.get_order(id);
        }
        //Toast.makeText(NotificationsDataActivity.this,id, Toast.LENGTH_LONG).show();
    }

    public void setData(OrderModel orderModel) {
        pd.dismiss();
        activityNotificationsDataBinding.txtOrderNum.setText(orderModel.getOrders().getOrderId());
        if (orderModel.getOrders().getHaletTalab().equals("neworder")){
            activityNotificationsDataBinding.txtOrderStatus.setText("طلب جديد");
        }else if (orderModel.getOrders().getHaletTalab().equals("inprogress")){
            activityNotificationsDataBinding.txtOrderStatus.setText("قيد التنفيذ");
        }else if (orderModel.getOrders().getHaletTalab().equals("tawsel")){
            activityNotificationsDataBinding.txtOrderStatus.setText("قيد التوصيل");
        }else if (orderModel.getOrders().getHaletTalab().equals("completed")){
            activityNotificationsDataBinding.txtOrderStatus.setText("تم التوصيل");
        }else if (orderModel.getOrders().getHaletTalab().equals("cancelled")){
            activityNotificationsDataBinding.txtOrderStatus.setText("ملغي");
        }
        activityNotificationsDataBinding.txtTotalPrice.setText(orderModel.getOrders().getAllSum()+"");
        activityNotificationsDataBinding.totalPrice.setText("ريال");
        activityNotificationsDataBinding.txtOrderAddress.setText(orderModel.getOrders().getAddress());
        activityNotificationsDataBinding.txtOrderDate.setText(orderModel.getOrders().getOrderDate()+" "+orderModel.getOrders().getOrderTime());
        if (orderModel.getOrders().getPayMethod().equals("1")){
            activityNotificationsDataBinding.txtPaymentMethods.setText("عند الإستلام");
        }else if (orderModel.getOrders().getPayMethod().equals("2")){
            activityNotificationsDataBinding.txtPaymentMethods.setText("تحويل بنكي");
        }else if (orderModel.getOrders().getPayMethod().equals("3")){
            activityNotificationsDataBinding.txtPaymentMethods.setText("بطاقة إئتمان");
        }
        orderDetailAdapter = new OrderDetailAdapter(orderModel.getOrders().getOrderDetails(),this);
        layoutManager = new LinearLayoutManager(this);
        activityNotificationsDataBinding.orderDetailsRecycler.setHasFixedSize(true);
        activityNotificationsDataBinding.orderDetailsRecycler.setLayoutManager(layoutManager);
        activityNotificationsDataBinding.orderDetailsRecycler.setAdapter(orderDetailAdapter);
    }
}