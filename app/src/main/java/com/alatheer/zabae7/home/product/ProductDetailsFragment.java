package com.alatheer.zabae7.home.product;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.Backpressedlistener;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentProductDetailsBinding;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.home2.AllProductsCity;
import com.alatheer.zabae7.home.home2.CuttingOption;
import com.alatheer.zabae7.home.home2.CuttingOptionAdapter;
import com.alatheer.zabae7.home.home2.CuttingOptionHead;
import com.alatheer.zabae7.home.home2.CuttingOptionHeadAdapter;
import com.alatheer.zabae7.home.home2.ProductSize;
import com.alatheer.zabae7.home.home2.ProductSizeAdapter;
import com.alatheer.zabae7.home.home2.TaghlifAdapter;
import com.alatheer.zabae7.home.home2.TaghlifOption;
import com.alatheer.zabae7.login.LoginActivity2;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.alatheer.zabae7.payment.PersonalDataActivity;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.singleton.OrderItemsSingleTone;
import com.alatheer.zabae7.updateproduct.ProductDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsFragment extends Fragment implements Backpressedlistener {
    FragmentProductDetailsBinding fragmentProductDetailsBinding;
    ProductDetailsViewModel productDetailsViewModel;
    Bundle bundle;
    AllProductsCity allProduct;
    ProductDetails productDetails;
    OrderItemsSingleTone orderItemsSingleTone;
    Integer count = 1;
    OrderItemList orderItemList;
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
    public static Backpressedlistener backpressedlistener;
    String city_id;
    Integer flag;
    User user;
    DatabaseClass databaseClass;
    Integer totalPrice;
    List<String> sizeModeltitleList,cuttingOptionstitleList,taghlifOptionstitleList,cuttingOptionsheadtitleList;
    String size_id="0",size_name,size_price,cutting_id="0",cutting_name="",cutting_price="0",package_id="0",package_name="",package_price="0",cutting_head_id="0",cutting__head_name="",cutting_head_price="0",user_id,product_id,more_data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProductDetailsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_details, container, false);
        View view = fragmentProductDetailsBinding.getRoot();
        productDetailsViewModel = new ProductDetailsViewModel(getActivity(),this);
        fragmentProductDetailsBinding.setProductdetailsviewmodel(productDetailsViewModel);
        sizeModelList = new ArrayList<>();
        cuttingOptionList = new ArrayList<>();
        taghlifOptionList = new ArrayList<>();
        sizeModeltitleList = new ArrayList<>();
        cuttingOptionstitleList = new ArrayList<>();
        taghlifOptionstitleList = new ArrayList<>();
        cuttingOptionsheadtitleList = new ArrayList<>();
        bundle = this.getArguments();
        flag = bundle.getInt("flag");
        if (flag != 2){
            allProduct = bundle.getParcelable("model");
            //Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
            setData(allProduct);
        }else {
            try {
                more_data = bundle.getString("moredata");
                JSONObject json = new JSONObject(more_data);
                String id = (String) json.get("id");
                //Toast.makeText(getActivity(), id+"", Toast.LENGTH_SHORT).show();
                productDetailsViewModel.get_offer(Integer.parseInt(id));
            }catch (Exception e){
                Log.e("error66",e.getMessage());
                more_data = bundle.getString("moredata");
                //oast.makeText(getActivity(), id+"", Toast.LENGTH_SHORT).show();
                productDetailsViewModel.get_offer(Integer.parseInt(more_data));
            }
        }
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
        if (user != null){
            navBar.setVisibility(View.GONE);
        }else {
            navBar.setVisibility(View.GONE);
        }
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(),DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentProductDetailsBinding.txtBasketNum.setText(orderItemListList.size()+"");
        fragmentProductDetailsBinding.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                size_id = sizeModelList.get(i).getSizeId();
                if (!size_id.equals("0")){
                    if(sizeModelList.get(i).getOfferSize()==0){
                        size_name = sizeModelList.get(i).getSizeName();
                        size_price = sizeModelList.get(i).getPrice();
                        fragmentProductDetailsBinding.txtProductPrice.setVisibility(View.VISIBLE);
                        fragmentProductDetailsBinding.txtProductPrice.setText(size_price+"ريال");
                        fragmentProductDetailsBinding.txtProductOfferPrice.setVisibility(View.GONE);
                    }else {
                        size_name = sizeModelList.get(i).getSizeName();
                        size_price = sizeModelList.get(i).getOfferPrice();
                        fragmentProductDetailsBinding.txtProductOfferPrice.setVisibility(View.VISIBLE);
                        fragmentProductDetailsBinding.txtProductPrice.setVisibility(View.VISIBLE);
                        fragmentProductDetailsBinding.txtProductPrice.setText(sizeModelList.get(i).getOfferPrice()+"ريال");
                        fragmentProductDetailsBinding.txtProductOfferPrice.setText(sizeModelList.get(i).getPrice()+"ريال");
                    }
                }else {
                    fragmentProductDetailsBinding.txtProductOfferPrice.setVisibility(View.GONE);
                    fragmentProductDetailsBinding.txtProductPrice.setVisibility(View.GONE);
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
                if (!cutting_id.equals("0")) {
                    try {
                        cutting_price = cuttingOptionList.get(i).getPrice();
                        cutting_name = cuttingOptionList.get(i).getTitle();
                        fragmentProductDetailsBinding.txtCuttingPrice.setVisibility(View.VISIBLE);
                        if (!cutting_price.equals("0")){
                            fragmentProductDetailsBinding.txtCuttingPrice.setText(cutting_price+"ريال");
                        }else {
                            fragmentProductDetailsBinding.txtCuttingPrice.setText("مجاني");
                        }

                        TextView textView = (TextView) view;
                        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    } catch (Exception e) {
                        cutting_name = cuttingOptionList.get(i).getTitle();
                    }
                }else {
                    fragmentProductDetailsBinding.txtCuttingPrice.setVisibility(View.GONE);
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
                            fragmentProductDetailsBinding.txtTaghlifPrice.setText(package_price+"ريال");
                        }else {
                            fragmentProductDetailsBinding.txtTaghlifPrice.setText("مجاني");
                        }
                        TextView textView = (TextView) view;
                        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    } catch (Exception e) {
                        cutting_id = cuttingOptionList.get(i).getId();
                        cutting_name = cuttingOptionList.get(i).getTitle();
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
                            fragmentProductDetailsBinding.txtCuttingHeadPrice.setText(cutting_head_price+"ريال");
                        }else {
                            //Toast.makeText(getActivity(), cutting_head_price, Toast.LENGTH_SHORT).show();
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
                /*size_id = "0";
                size_name="";
                size_price="0";
                cutting_id="0";
                cutting_name="";
                cutting_price="0";
                package_id="0";
                package_name="";
                package_price="0";
                cutting_head_id="0";
                cutting__head_name="";
                cutting_head_price="0";*/
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
                if (flag != 2){
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
                    navBar.setVisibility(View.VISIBLE);
                }else {
                    getActivity().finish();
                }
                /*if (user != null){
                    navBar.setVisibility(View.VISIBLE);
                    navBar2.setVisibility(View.GONE);
                }else {
                    navBar2.setVisibility(View.VISIBLE);
                    navBar.setVisibility(View.GONE);
                }*/
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
                if(!size_id.equals("0")&&!cutting_id.equals("0")){
                    //Toast.makeText(getActivity(), cutting_id, Toast.LENGTH_SHORT).show();
                    orderItemList = new OrderItemList();
                    if (flag != 2){
                        orderItemList.setProduct_id(Integer.parseInt(allProduct.getProductId()));
                        orderItemList.setProduct_img("https://thabaeh.com/uploads/product/"+allProduct.getImage());
                        orderItemList.setProduct_name(allProduct.getProductName());
                    }else {
                        orderItemList.setProduct_id(Integer.parseInt(productDetails.getProductId()));
                        orderItemList.setProduct_img("https://thabaeh.com/uploads/product/"+productDetails.getImage());
                        orderItemList.setProduct_name(productDetails.getProductName());
                    }

                    orderItemList.setProduct_qty(count+"");
                    //orderItemList.setProduct_price(Double.parseDouble(allProduct.getProductPrice()));
                    //orderItemList.setTotal_price(Double.parseDouble(allProduct.getProductPrice())*count);
                    //Log.e("image",allProduct.getImage());
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
                        grandTotal();
                        //Toast.makeText(getActivity(), "تم اضافة المنتج بنجاح", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        databaseClass.getDao().editproduct(orderItemList);
                        grandTotal();

                        //Toast.makeText(getActivity(), "تم تعديل المنتج بنجاح", Toast.LENGTH_SHORT).show();
                        fragmentProductDetailsBinding.txtBasketNum.setText(databaseClass.getDao().getallproducts().size()+"");
                    }
                }else {
                    if (cutting_id.equals("0")){
                        Toast.makeText(getActivity(), "اختر التقطيع من فضلك", Toast.LENGTH_LONG).show();
                    }
                    if (size_id.equals("0")){
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
        fragmentProductDetailsBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
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
    }

    private Integer grandTotal() {
        totalPrice = 0;
        orderItemListList = databaseClass.getDao().getallproducts();
        //Log.e("totalprice",orderItemListList.get(0).getTotal_price());
        for(int i = 0 ; i < orderItemListList.size(); i++) {
            totalPrice += Integer.parseInt(orderItemListList.get(i).getTotal_price());
        }
        CreatePopup(totalPrice);
        //Toast.makeText(getActivity(), totalPrice+"", Toast.LENGTH_SHORT).show();
        return totalPrice;
    }
    public void UpdateList() {
        totalPrice = 0;
        Log.e("totalprice",orderItemListList.get(0).getTotal_price());
        for (int i = 0; i < orderItemListList.size(); i++) {
            totalPrice += Integer.parseInt(orderItemListList.get(i).getTotal_price());
        }
    }

    private void CreatePopup(Integer totalPrice1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_to_basket_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        Button continue_btn = view.findViewById(R.id.btn_continue);
        Button order_btn = view.findViewById(R.id.btn_order);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                getFragmentManager().popBackStack();
                BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
                FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                params.setMargins(0, 0, 0, 160);
                fragment_container.setLayoutParams(params);
                navBar.setVisibility(View.VISIBLE);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                intent.putExtra("totalprice",totalPrice1);
                //Toast.makeText(getContext(), totalPrice+"", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    /*private void initRecyclerView3() {
        cuttingOptionHeadAdapter = new CuttingOptionHeadAdapter(cuttingOptionHeadList,getActivity(),this);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        fragmentProductDetailsBinding.choppingHeadRecycler.setLayoutManager(layoutManager);
        fragmentProductDetailsBinding.choppingHeadRecycler.setAdapter(cuttingOptionHeadAdapter);
        fragmentProductDetailsBinding.choppingHeadRecycler.setHasFixedSize(true);
    }*/
    /*private void initRecyclerView4() {
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

    public void setData(AllProductsCity allProduct) {
        fragmentProductDetailsBinding.txtDetails.setText(Html.fromHtml(allProduct.getProductDesc().trim()));
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
       // fragmentProductDetailsBinding.productPrice.setText(allProduct.getProductPrice());
        Picasso.get().load("https://thabaeh.com/uploads/product/"+allProduct.getImage()).into(fragmentProductDetailsBinding.productImg);
    }

    private void setcuttingheadspinnerData(List<CuttingOptionHead> cuttingOptionHeadList, List<String> cuttingOptionsheadtitleList) {
        if (!cuttingOptionHeadList.isEmpty()){
            if (!cuttingOptionsheadtitleList.get(0).equals("اختر تقطيع الرأس")){
                cuttingOptionsheadtitleList.add(0, "اختر تقطيع الرأس");
                CuttingOptionHead cuttingOptionHead = new CuttingOptionHead();
                cuttingOptionHead.setTitle("اختر تقطيع الرأس");
                cuttingOptionHead.setId("0");
                cuttingOptionHeadList.add(0,cuttingOptionHead);
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,cuttingOptionsheadtitleList);
        fragmentProductDetailsBinding.cuttingHeadSpinner.setAdapter(arrayAdapter);
    }

    private void settaghlifspinnerData(List<TaghlifOption> taghlifOptionList, List<String> taghlifOptionstitleList) {
        if (!taghlifOptionList.isEmpty()){
            if (!taghlifOptionstitleList.get(0).equals("اختر نوع التغليف")){
                taghlifOptionstitleList.add(0, "اختر نوع التغليف");
                TaghlifOption taghlifOption = new TaghlifOption();
                taghlifOption.setTitle("اختر نوع التغليف");
                taghlifOption.setId("0");
                taghlifOptionList.add(0,taghlifOption);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,taghlifOptionstitleList);
            fragmentProductDetailsBinding.taghlifSpinner.setAdapter(arrayAdapter);
        }
    }

    private void setcuttingspinnerData(List<CuttingOption> cuttingOptionList, List<String> cuttingOptionstitleList) {
        if (!cuttingOptionList.isEmpty()){
            if (!cuttingOptionstitleList.get(0).equals("اختر التقطيع")){
                cuttingOptionstitleList.add(0, "اختر التقطيع");
                CuttingOption cuttingOption = new CuttingOption();
                cuttingOption.setTitle("اختر التقطيع");
                cuttingOption.setId("0");
                cuttingOptionList.add(0,cuttingOption);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,cuttingOptionstitleList);
            fragmentProductDetailsBinding.cuttingSpinner.setAdapter(arrayAdapter);
        }
    }

    private void setsizesspinnerData(List<String> sizeModeltitleList, List<ProductSize> sizeModelList) {
        if (!sizeModeltitleList.get(0).equals("اختر الحجم")){
            sizeModeltitleList.add(0, "اختر الحجم");
            ProductSize productSize = new ProductSize();
            productSize.setSizeName("اختر الحجم");
            productSize.setSizeId("0");
            sizeModelList.add(0,productSize);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,sizeModeltitleList);
        fragmentProductDetailsBinding.sizeSpinner.setAdapter(arrayAdapter);
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

    public void setData2(ProductDetails productDetails) {
        try {
            this.productDetails = productDetails;
            fragmentProductDetailsBinding.txtDetails.setText(Html.fromHtml(productDetails.getProductDesc().trim()));
            fragmentProductDetailsBinding.txtProductName.setText(productDetails.getProductName().trim());
            sizeModelList = productDetails.getProductSizes();
            for (ProductSize productSize : sizeModelList) {
                sizeModeltitleList.add(productSize.getSizeName());
            }
            setsizesspinnerData(sizeModeltitleList, sizeModelList);
            cuttingOptionList = productDetails.getCuttingOption();
            for (CuttingOption cuttingOption : cuttingOptionList) {
                cuttingOptionstitleList.add(cuttingOption.getTitle());
            }
            setcuttingspinnerData(cuttingOptionList, cuttingOptionstitleList);
            taghlifOptionList = productDetails.getTaghlifOption();
            for (TaghlifOption taghlifOption : taghlifOptionList) {
                taghlifOptionstitleList.add(taghlifOption.getTitle());
            }
            settaghlifspinnerData(taghlifOptionList, taghlifOptionstitleList);
            cuttingOptionHeadList = productDetails.getCuttingOptionHead();
            for (CuttingOptionHead cuttingOptionHead : cuttingOptionHeadList) {
                cuttingOptionsheadtitleList.add(cuttingOptionHead.getTitle());
            }
            setcuttingheadspinnerData(cuttingOptionHeadList, cuttingOptionsheadtitleList);
            //initRecyclerView3();
            //initRecyclerView4();
            // fragmentProductDetailsBinding.productPrice.setText(allProduct.getProductPrice());
            Picasso.get().load("https://thabaeh.com/uploads/product/"+productDetails.getImage()).into(fragmentProductDetailsBinding.productImg);
        }catch (Exception e){
            cuttingOptionList.size();
            Log.e("llllll",e.getMessage());
        }

    }
    @Override
    public void onPause() {
        // passing null value
        // to backpressedlistener
        backpressedlistener=null;
        super.onPause();
        BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
        BottomNavigationView navBar2 = getActivity().findViewById(R.id.home_bottomnavigation2);
        FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 160);
        fragment_container.setLayoutParams(params);
        navBar.setVisibility(View.VISIBLE);
    }


    // Overriding onResume() method
    @Override
    public void onResume() {
        super.onResume();
        // passing context of fragment
        //  to backpressedlistener
        backpressedlistener=this;
        /*BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
        //BottomNavigationView navBar2 = getActivity().findViewById(R.id.home_bottomnavigation2);
        FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 160);
        fragment_container.setLayoutParams(params);
        navBar.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void onBackPressed() {
        if (flag != 2){
            Toast.makeText(getActivity(), "back", Toast.LENGTH_SHORT).show();
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
            navBar.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(getActivity(), "finish", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }
}
