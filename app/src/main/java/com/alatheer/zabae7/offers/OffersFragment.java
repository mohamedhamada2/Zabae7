package com.alatheer.zabae7.offers;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.Backpressedlistener;
import com.alatheer.zabae7.data.CitySharedPreferance;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentOffersBinding;
import com.alatheer.zabae7.databinding.FragmentOrdersBinding;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.home2.AllProductsCity;
import com.alatheer.zabae7.home.home2.HomeFragmentViewModel;
import com.alatheer.zabae7.home.home2.ProductsAdapter;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.alatheer.zabae7.signup.AllCity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class OffersFragment extends Fragment implements Backpressedlistener {
    FragmentOffersBinding fragmentOffersBinding;
    OfferViewModel offerViewModel;
    HomeActivity homeActivity;
    String city_id;
    ProductAdapter productsAdapter;
    LinearLayoutManager layoutManager;
    CitySharedPreferance citySharedPreferance;
    AllCity allCity;
    UserSharedPreference  userSharedPreference;
    User user;
    DatabaseClass databaseClass;
    List<OrderItemList> orderItemListList;
    String user_id;
    public static Backpressedlistener backpressedlistener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOffersBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_offers, container, false);
        View view = fragmentOffersBinding.getRoot();
        offerViewModel = new OfferViewModel(getActivity(),this);
        fragmentOffersBinding.setOfferViewModel(offerViewModel);
        //orderItemListList = orderItemsSingleTone.getOrderItemList();
        homeActivity = (HomeActivity) getActivity();
        citySharedPreferance = CitySharedPreferance.getInstance();
        allCity = citySharedPreferance.Get_UserData(getContext());
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(getContext());
        if (user != null){
            user_id = user.getMember().getUserId();
            offerViewModel.getNotifications(user_id);
        }else {
            offerViewModel.getNotifications("0");
        }
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(), DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentOffersBinding.txtBasketNum.setText(orderItemListList.size()+"");
        city_id = allCity.getCityId();
        offerViewModel.getProducts(city_id);
        fragmentOffersBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        fragmentOffersBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNotifications();
            }
        });
        fragmentOffersBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return  view;
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
    }
    public void GotoBasket() {
        Fragment fragment = new BasketFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flag","2");
        fragment.setArguments(bundle);
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
        // fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("BasketFragment").setCustomAnimations(R.anim.slide_in,  // enter
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
    }
    public void initRecycler(List<AllProductsCity> allProductList) {
        fragmentOffersBinding.productsRecycler.setHasFixedSize(true);
        productsAdapter = new ProductAdapter(getActivity(),allProductList,this);
        fragmentOffersBinding.productsRecycler.setAdapter(productsAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentOffersBinding.productsRecycler.setLayoutManager(layoutManager);
    }

    public void sendData(AllProductsCity allProductsCity) {
        Fragment fragment = new ProductDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", allProductsCity);
        bundle.putInt("flag",1);
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("ProductDetailsFragment").setCustomAnimations(R.anim.slide_in,  // enter
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
        if (user != null){
            navBar.setVisibility(View.GONE);
        }else {
            navBar.setVisibility(View.GONE);
        }
    }

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentOffersBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentOffersBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentOffersBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

    }
    @Override
    public void onPause() {
        // passing null value
        // to backpressedlistener
        backpressedlistener = null;
        super.onPause();
    }


    // Overriding onResume() method
    @Override
    public void onResume() {
        super.onResume();
        // passing context of fragment
        //  to backpressedlistener
        backpressedlistener = this;

    }

}