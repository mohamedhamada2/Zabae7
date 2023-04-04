package com.alatheer.zabae7.home.home2;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.Backpressedlistener;
import com.alatheer.zabae7.data.CitySharedPreferance;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentHomeBinding;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.countries.CountriesActivity;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.singleton.OrderItemsSingleTone;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class HomeFragment extends Fragment implements Backpressedlistener {
    FragmentHomeBinding fragmentHomeBinding;
    HomeFragmentViewModel homeFragmentViewModel;
    ProductsAdapter productsAdapter;
    RecyclerView.LayoutManager layoutManager;
    OrderItemsSingleTone orderItemsSingleTone;
    List<OrderItemList> orderItemListList;
    String city_id  ;
    DatabaseClass databaseClass;
    UserSharedPreference userSharedPreference;
    User user;
    List<AllCity> allCities;
    ArrayAdapter<String> allcitiesadapter;
    HomeActivity homeActivity;
    CitySharedPreferance citySharedPreferance;
    AllCity allCity;
    String user_id;
    public static Backpressedlistener backpressedlistener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        View view = fragmentHomeBinding.getRoot();
        homeFragmentViewModel = new HomeFragmentViewModel(getActivity(),this);
        fragmentHomeBinding.setHomeViewModel(homeFragmentViewModel);
        //orderItemListList = orderItemsSingleTone.getOrderItemList();
        homeActivity = (HomeActivity) getActivity();
        citySharedPreferance = CitySharedPreferance.getInstance();
        allCity = citySharedPreferance.Get_UserData(getContext());
        city_id = allCity.getCityId();
        getSharedPreferance();
        homeFragmentViewModel.getProducts(city_id);
        fragmentHomeBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                GotoNotifications();
            }
        });
        //homeFragmentViewModel.getCities();
        /*fragmentHomeBinding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_id = allCities.get(position).getCityId();
                homeFragmentViewModel.getProducts(city_id);
                try {
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.lightsilver));

                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(),DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();

        fragmentHomeBinding.txtBasketNum.setText(orderItemListList.size()+"");
        fragmentHomeBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        fragmentHomeBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().finish();
            }
        });
        return view;
    }

    private void GotoNotifications() {
        Fragment fragment = new NotificationsFragment();
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
        // fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
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
        navBar2.setVisibility(View.GONE);
        //Toast.makeText(homeActivity, "success", Toast.LENGTH_SHORT).show();
    }

    private void getSharedPreferance() {
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(getActivity());

        if (user != null){
            user_id = user.getMember().getUserId();
            homeFragmentViewModel.getNotifications(user_id);
        }else {
            homeFragmentViewModel.getNotifications("0");
        }
    }

    public void initRecycler(List<AllProductsCity> allProductList) {
        fragmentHomeBinding.productsRecycler.setHasFixedSize(true);
        productsAdapter = new ProductsAdapter(getActivity(),allProductList,this);
        fragmentHomeBinding.productsRecycler.setAdapter(productsAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentHomeBinding.productsRecycler.setLayoutManager(layoutManager);
    }

    public void GotoBasket() {
        Fragment fragment = new BasketFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flag","1");
        fragment.setArguments(bundle);
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
       // fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("BasketFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();

    }

    public void setBasketItems(int size) {
        fragmentHomeBinding.txtBasketNum.setText(size+"");
    }

    public void sendData(AllProductsCity allProduct) {
        Fragment fragment = new ProductDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", allProduct);
        bundle.putInt("flag",1);
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().addToBackStack("ProductDetailsFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();

    }

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentHomeBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentHomeBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentHomeBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

    }
    @Override
    public void onPause() {
        // passing null value
        // to backpressedlistener
        backpressedlistener=null;
        super.onPause();
    }


    // Overriding onResume() method
    @Override
    public void onResume() {
        super.onResume();
        // passing context of fragment
        //  to backpressedlistener
        backpressedlistener=this;
    }

    /*public void initSpinner(List<String> allcitiestitle, List<AllCity> allCities) {
        allcitiesadapter = new ArrayAdapter<String>(homeActivity, R.layout.spinner_item, allcitiestitle);
        fragmentHomeBinding.citySpinner.setAdapter(allcitiesadapter);
        this.allCities = allCities;
    }*/
}
