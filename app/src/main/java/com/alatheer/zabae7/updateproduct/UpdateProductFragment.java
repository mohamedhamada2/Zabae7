package com.alatheer.zabae7.updateproduct;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentProductDetailsBinding;
import com.alatheer.zabae7.databinding.FragmentUpdateProductBinding;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.home2.AllProductsCity;
import com.alatheer.zabae7.home.home2.CuttingOption;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.updateproduct.CuttingOptionAdapter;
import com.alatheer.zabae7.home.home2.CuttingOptionHead;
import com.alatheer.zabae7.updateproduct.CuttingOptionHeadAdapter;
import com.alatheer.zabae7.home.home2.ProductSize;
import com.alatheer.zabae7.updateproduct.ProductSizeAdapter;
import com.alatheer.zabae7.updateproduct.TaghlifAdapter;
import com.alatheer.zabae7.home.home2.TaghlifOption;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.home.product.ProductDetailsViewModel;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.singleton.OrderItemsSingleTone;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UpdateProductFragment extends Fragment {
    FragmentUpdateProductBinding fragmentProductDetailsBinding;
    UpdateProductViewModel productDetailsViewModel;
    Bundle bundle;
    OrderItemsSingleTone orderItemsSingleTone;
    Integer count;
    OrderItemList orderItemList,orderItemList2;
    List<OrderItemList> orderItemListList;
    UserSharedPreference userSharedPreference;
    ProductSizeAdapter productSizeAdapter;
    CuttingOptionAdapter cuttingOptionAdapter;
    CuttingOptionHeadAdapter cuttingOptionHeadAdapter;
    TaghlifAdapter taghlifAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<ProductSize> sizeModelList;
    List<CuttingOption> cuttingOptionList;
    List<TaghlifOption> taghlifOptionList;
    List<CuttingOptionHead> cuttingOptionHeadList;
    String city_id,flag;
    User user;
    DatabaseClass databaseClass;
    List<String> sizeModeltitleList,cuttingOptionstitleList,cuttingOptionsheadtitleList,taghlifOptionstitleList;
    ProductDetails allProduct;
    String size_id,size_name,size_price,cutting_id="0",cutting_name="",cutting_price="0",package_id="0",package_name="",package_price="0",cutting_head_id="0",cutting__head_name="",cutting_head_price="0",user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProductDetailsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_update_product, container, false);
        View view = fragmentProductDetailsBinding.getRoot();
        productDetailsViewModel = new UpdateProductViewModel(getContext(),this);
        sizeModelList = new ArrayList<>();
        cuttingOptionList = new ArrayList<>();
        taghlifOptionList = new ArrayList<>();
        sizeModeltitleList = new ArrayList<>();
        cuttingOptionstitleList = new ArrayList<>();
        taghlifOptionstitleList = new ArrayList<>();
        cuttingOptionsheadtitleList = new ArrayList<>();
        bundle = getArguments();
        orderItemList2 = (OrderItemList) bundle.getSerializable("order");
        size_id = orderItemList2.getSize_id();
        cutting_id = orderItemList2.getCutting_id();
        cutting_head_id = orderItemList2.getCutting_head_id();
        package_id = orderItemList2.getPackag_id();
        count = Integer.parseInt(orderItemList2.getProduct_qty());
        fragmentProductDetailsBinding.txtProductAmount.setText(count+"");
        productDetailsViewModel.getProductData(orderItemList2.getProduct_id());
        //sizeModelList2 = new ArrayList<>();
        //sizeModelList3 = new ArrayList<>();
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(getActivity());
        if (user != null){
            user_id = user.getMember().getUserId();
            productDetailsViewModel.getNotifications(user_id);
        }else {
            productDetailsViewModel.getNotifications("0");
        }
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
        fragmentProductDetailsBinding.sizeSpinner.setEnabled(false);
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(),DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        //setData(allProduct);
        fragmentProductDetailsBinding.txtBasketNum.setText(orderItemListList.size()+"");
        fragmentProductDetailsBinding.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                size_id = sizeModelList.get(i).getSizeId();
                size_name = sizeModelList.get(i).getSizeName();
                if(sizeModelList.get(i).getOfferSize()==0){
                    size_price = sizeModelList.get(i).getPrice();
                    fragmentProductDetailsBinding.txtProductPrice.setText(size_price+"ريال");
                    fragmentProductDetailsBinding.txtProductOfferPrice.setVisibility(View.GONE);
                }else {
                    size_price = sizeModelList.get(i).getOfferPrice();
                    fragmentProductDetailsBinding.txtProductOfferPrice.setVisibility(View.VISIBLE);
                    fragmentProductDetailsBinding.txtProductPrice.setText(sizeModelList.get(i).getOfferPrice()+"ريال");
                    fragmentProductDetailsBinding.txtProductOfferPrice.setText(sizeModelList.get(i).getPrice()+"ريال");
                }
                try {
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }catch (Exception e){

                }
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fragmentProductDetailsBinding.cuttingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cutting_id = cuttingOptionList.get(i).getId();
                cutting_price = cuttingOptionList.get(i).getPrice();
                cutting_name = cuttingOptionList.get(i).getTitle();
                if (!cutting_price.equals("0")){
                    fragmentProductDetailsBinding.txtCuttingPrice.setText(cutting_price);
                }else {
                    fragmentProductDetailsBinding.txtCuttingPrice.setText("مجاني");
                }
                try {
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }catch (Exception e){

                }
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fragmentProductDetailsBinding.taghlifSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                package_id = taghlifOptionList.get(i).getId();
                if (!package_id.equals("0")) {
                    try {
                        package_name = taghlifOptionList.get(i).getTitle();
                        package_price = taghlifOptionList.get(i).getPrice();
                        fragmentProductDetailsBinding.txtTaghlifPrice.setVisibility(View.VISIBLE);
                        if (!package_price.equals("0")){
                            fragmentProductDetailsBinding.txtTaghlifPrice.setText(package_price);
                        }else {
                            fragmentProductDetailsBinding.txtTaghlifPrice.setText("مجاني");
                        }
                        TextView textView = (TextView) view;
                        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    } catch (Exception e) {
                        package_name = taghlifOptionList.get(i).getTitle();
                    }
                }else {
                    fragmentProductDetailsBinding.txtTaghlifPrice.setVisibility(View.GONE);
                }
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fragmentProductDetailsBinding.cuttingHeadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cutting_head_id = cuttingOptionHeadList.get(i).getId();
                if (!cutting_head_id.equals("0")) {
                    try {
                        cutting__head_name = cuttingOptionHeadList.get(i).getTitle();
                        cutting_head_price = cuttingOptionHeadList.get(i).getPrice();
                        fragmentProductDetailsBinding.txtCuttingHeadPrice.setVisibility(View.VISIBLE);
                        if (!cutting_head_price.equals("0")){
                            fragmentProductDetailsBinding.txtCuttingHeadPrice.setText(cutting_head_price);
                        }else {
                            fragmentProductDetailsBinding.txtCuttingHeadPrice.setText("مجاني");
                        }
                        TextView textView = (TextView) view;
                        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    } catch (Exception e) {
                        cutting__head_name = cuttingOptionHeadList.get(i).getTitle();
                    }
                }else {
                    fragmentProductDetailsBinding.txtCuttingHeadPrice.setVisibility(View.GONE);
                }
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fragmentProductDetailsBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size_id = "";
                size_name="";
                size_price="";
                cutting_id="0";
                cutting_name="";
                cutting_price="0";
                package_id="0";
                package_name="";
                package_price="0";
                cutting_head_id="0";
                cutting__head_name="";
                cutting_head_price="0";
                GotoBasket();
            }
        });
        fragmentProductDetailsBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plus();
                fragmentProductDetailsBinding.txtProductAmount.setText(count+"");
            }
        });
        fragmentProductDetailsBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
                BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
                BottomNavigationView navBar2 = getActivity().findViewById(R.id.home_bottomnavigation2);
                FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                params.setMargins(0, 0, 0, 160);
                fragment_container.setLayoutParams(params);
                if (user != null){
                    navBar.setVisibility(View.VISIBLE);
                    navBar2.setVisibility(View.GONE);
                }else {
                    navBar2.setVisibility(View.VISIBLE);
                    navBar.setVisibility(View.GONE);
                }
            }
        });
        fragmentProductDetailsBinding.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minus();
                fragmentProductDetailsBinding.txtProductAmount.setText(count+"");
            }
        });
        fragmentProductDetailsBinding.addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(size_id)){
                    orderItemList = new OrderItemList();
                    orderItemList.setProduct_id(orderItemList2.getProduct_id());
                    orderItemList.setProduct_qty(count+"");
                    //orderItemList.setTotal_price(Double.parseDouble()*count);
                    //Log.e("image",allProduct.getImage());
                    orderItemList.setProduct_img("https://thabaeh.com/uploads/product/"+allProduct.getImage());
                    orderItemList.setProduct_name(allProduct.getProductName());
                    orderItemList.setSize_id(size_id);
                    orderItemList.setSize_name(size_name);
                    orderItemList.setSize_price(size_price);
                    orderItemList.setCutting_id(cutting_id);
                    orderItemList.setCutting_name(cutting_name);
                    orderItemList.setCutting_price(cutting_price);
                    orderItemList.setPackag_id(package_id);
                    orderItemList.setPackag_name(package_name);
                    orderItemList.setPackag_price(package_price);
                    orderItemList.setCutting_head_id(cutting_head_id);
                    orderItemList.setCutting_head_price(cutting_head_price);
                    orderItemList.setCutting_head_name(cutting__head_name);
                    Integer package_price2 = Integer.parseInt(package_price);
                    Integer cutting_price2 = Integer.parseInt(cutting_price);
                    Integer cutting_head_price2 = Integer.parseInt(cutting_head_price);

                    Integer addition_price = package_price2+cutting_price2+cutting_head_price2;
                    int price = Integer.parseInt(size_price)* count;
                    int total_price = price+addition_price;
                    orderItemList.setTotal_price(total_price+"");
                    Log.e("total_price",total_price+"");
                        /*for (int i = 0;i<orderItemListList.size();i++){
                            if(orderItemListList.get(i).getProduct_id()== orderItemList.getProduct_id() ){
                                Toast.makeText(getActivity(), "updated successfully", Toast.LENGTH_SHORT).show();
                                databaseClass.getDao().editproduct(orderItemList);
                                //orderItemsSingleTone.UpdateProduct(orderItemList);
                            }else {
                                Toast.makeText(getActivity(), "added successfully", Toast.LENGTH_SHORT).show();
                                databaseClass.getDao().AddProduct(orderItemList);
                                fragmentProductDetailsBinding.txtBasketNum.setText(orderItemListList.size()+"");
                            }
                        }*/
                    try {
                        databaseClass.getDao().AddProduct(orderItemList);
                        fragmentProductDetailsBinding.txtBasketNum.setText(databaseClass.getDao().getallproducts().size()+"");
                        Toast.makeText(getActivity(), "تم اضافة المنتج بنجاح", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.e("database_error",e.getMessage());
                        databaseClass.getDao().editproduct(orderItemList);
                        Toast.makeText(getActivity(), "تم تعديل المنتج بنجاح", Toast.LENGTH_SHORT).show();
                        fragmentProductDetailsBinding.txtBasketNum.setText(databaseClass.getDao().getallproducts().size()+"");
                    }
                }else {
                    if (TextUtils.isEmpty(size_id)){
                        Toast.makeText(getActivity(), "اختر الحجم من فضلك", Toast.LENGTH_LONG).show();
                    }
                }
                //try {
                //userSharedPreference = UserSharedPreference.getInstance();
                //user = userSharedPreference.Get_UserData(getActivity());
                //if (user != null){



                    /*else {
                        startActivity(new Intent(getActivity(), LoginActivity2.class));
                        Toast.makeText(getActivity(), "برجاء تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                    }*/
                /*}catch (Exception e){
                    Log.e("error",e.toString());
                    startActivity(new Intent(getActivity(), LoginActivity2.class));
                    Toast.makeText(getActivity(), "برجاء تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        fragmentProductDetailsBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        fragmentProductDetailsBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNotifications();
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
    }

    /*private void initRecyclerView3() {
        cuttingOptionHeadAdapter = new CuttingOptionHeadAdapter(cuttingOptionHeadList,getActivity(),this,cutting_id);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        fragmentProductDetailsBinding.choppingHeadRecycler.setLayoutManager(layoutManager);
        fragmentProductDetailsBinding.choppingHeadRecycler.setAdapter(cuttingOptionHeadAdapter);
        fragmentProductDetailsBinding.choppingHeadRecycler.setHasFixedSize(true);
    }
    private void initRecyclerView4() {
        taghlifAdapter = new TaghlifAdapter(taghlifOptionList,getActivity(),this);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        fragmentProductDetailsBinding.recycler4.setLayoutManager(layoutManager);
        fragmentProductDetailsBinding.recycler4.setAdapter(taghlifAdapter);
        fragmentProductDetailsBinding.recycler4.setHasFixedSize(true);
    }*/


    private void GotoBasket() {
        Fragment fragment = new BasketFragment();
        Bundle bundle = new Bundle();
        bundle.putString("city_id",city_id);
        bundle.putString("flag","2");
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().addToBackStack("BasketFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
    }

    private void minus() {
        if(count>1){
            count = count-1;
            //total_price = count*size_price;
        }else {
            count = 1;
            //total_price = size_price;
        }
    }

    private void plus() {
        count = count+1;
    }

    public void setData(ProductDetails allProduct) {
        this.allProduct = allProduct;
        fragmentProductDetailsBinding.txtDetails.setText(Html.fromHtml(allProduct.getProductDesc()));
        fragmentProductDetailsBinding.txtProductName.setText(allProduct.getProductName().trim());
        sizeModelList = allProduct.getProductSizes();
        for (ProductSize productSize : sizeModelList) {
            sizeModeltitleList.add(productSize.getSizeName());
        }
        setsizesspinnerData(sizeModeltitleList, sizeModelList);
        cuttingOptionList = allProduct.getCuttingOption();
        for (CuttingOption cuttingOption : cuttingOptionList) {
            cuttingOptionstitleList.add(cuttingOption.getTitle());
        }
        setcuttingspinnerData(cuttingOptionList, cuttingOptionstitleList);
        taghlifOptionList = allProduct.getTaghlifOption();
        for (TaghlifOption taghlifOption : taghlifOptionList) {
            taghlifOptionstitleList.add(taghlifOption.getTitle());
        }
        settaghlifspinnerData(taghlifOptionList, taghlifOptionstitleList);
        cuttingOptionHeadList = allProduct.getCuttingOptionHead();
        for (CuttingOptionHead cuttingOptionHead : cuttingOptionHeadList) {
            cuttingOptionsheadtitleList.add(cuttingOptionHead.getTitle());
        }
        setcuttingheadspinnerData(cuttingOptionHeadList, cuttingOptionsheadtitleList);
        //initRecyclerView3();
        //initRecyclerView4();
        //cuttingOptionHeadList = allProduct.getCuttingOptionHead();
        // fragmentProductDetailsBinding.productPrice.setText(allProduct.getProductPrice());
        Picasso.get().load("https://thabaeh.com/uploads/product/"+allProduct.getImage()).into(fragmentProductDetailsBinding.productImg);
    }

    private void setcuttingheadspinnerData(List<CuttingOptionHead> cuttingOptionHeadList, List<String> cuttingOptionsheadtitleList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,cuttingOptionsheadtitleList);
        fragmentProductDetailsBinding.cuttingHeadSpinner.setAdapter(arrayAdapter);
        String compareValue = cutting_head_id;
        if (compareValue != null) {
            for (CuttingOptionHead cuttingOptionHead:cuttingOptionHeadList){
                if((cuttingOptionHead.getId()).equals(compareValue)){
                    int spinnerPosition = arrayAdapter.getPosition(cuttingOptionHead.getTitle());
                    fragmentProductDetailsBinding.cuttingHeadSpinner.setSelection(spinnerPosition);
                    fragmentProductDetailsBinding.txtCuttingHeadPrice.setText(package_price);
                    //profileViewModel.getadditions(datum.getSubCategoryId());
                }
            }
        }
    }

    private void settaghlifspinnerData(List<TaghlifOption> taghlifOptionList, List<String> taghlifOptionstitleList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,taghlifOptionstitleList);
        fragmentProductDetailsBinding.taghlifSpinner.setAdapter(arrayAdapter);
        String compareValue = package_id;
        if (compareValue != null) {
            for (TaghlifOption taghlifOption:taghlifOptionList){
                if((taghlifOption.getId()).equals(compareValue)){
                    int spinnerPosition = arrayAdapter.getPosition(taghlifOption.getTitle());
                    fragmentProductDetailsBinding.taghlifSpinner.setSelection(spinnerPosition);
                    fragmentProductDetailsBinding.txtTaghlifPrice.setText(package_price);
                    //profileViewModel.getadditions(datum.getSubCategoryId());
                }
            }
        }
    }

    private void setcuttingspinnerData(List<CuttingOption> cuttingOptionList, List<String> cuttingOptionstitleList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,cuttingOptionstitleList);
        fragmentProductDetailsBinding.cuttingSpinner.setAdapter(arrayAdapter);
        String compareValue = cutting_id;
        if (compareValue != null) {
            for (CuttingOption cuttingOption:cuttingOptionList){
                if((cuttingOption.getId()).equals(compareValue)){
                    int spinnerPosition = arrayAdapter.getPosition(cuttingOption.getTitle());
                    fragmentProductDetailsBinding.cuttingSpinner.setSelection(spinnerPosition);
                    //profileViewModel.getadditions(datum.getSubCategoryId());
                }
            }
        }
    }

    private void setsizesspinnerData(List<String> sizeModeltitleList, List<ProductSize> sizeModelList) {
        String compareValue = size_id;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,sizeModeltitleList);
        fragmentProductDetailsBinding.sizeSpinner.setAdapter(arrayAdapter);
        if (compareValue != null) {
            for (ProductSize productSize:sizeModelList){
                if((productSize.getSizeId()).equals(compareValue)){
                    int spinnerPosition = arrayAdapter.getPosition(productSize.getSizeName());
                    fragmentProductDetailsBinding.sizeSpinner.setSelection(spinnerPosition);
                    //profileViewModel.getadditions(datum.getSubCategoryId());
                }
            }
        }
    }

    public void send_size(ProductSize productSize) {
        size_id = productSize.getSizeId();
        size_name = productSize.getSizeName();
        size_price = productSize.getPrice();
    }

    public void send_package(TaghlifOption taghlifOption) {
        package_id = taghlifOption.getId();
        package_name = taghlifOption.getTitle();
        package_price = taghlifOption.getPrice();
    }

    public void send_cutting(CuttingOption cuttingOption) {
        cutting_id = cuttingOption.getId();
        cutting_name = cuttingOption.getTitle();
        cutting_price = cuttingOption.getPrice();
    }

    public void send_cutting_head(CuttingOptionHead cuttingOptionHead) {
        cutting_head_id = cuttingOptionHead.getId();
        cutting__head_name = cuttingOptionHead.getTitle();
        cutting_head_price = cuttingOptionHead.getPrice();
    }

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentProductDetailsBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentProductDetailsBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentProductDetailsBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }
}