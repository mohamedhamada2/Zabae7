package com.alatheer.zabae7.home.home2;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.singleton.OrderItemsSingleTone;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kr.co.prnd.readmore.ReadMoreTextView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder> {
    Context context;
    List<AllProductsCity> allProductList;
    HomeFragment homeFragment;
    OrderItemsSingleTone orderItemsSingleTone;
    OrderItemList orderItemList;
    List<OrderItemList> orderItemListList;
    UserSharedPreference userSharedPreference;
    HomeActivity homeActivity;
    User user;
    Integer count = 1;
    public ProductsAdapter(Context context, List<AllProductsCity> allProductList, HomeFragment homeFragment) {
        this.context = context;
        this.allProductList = allProductList;
        this.homeFragment = homeFragment;
        homeActivity = (HomeActivity) context;

    }

    @NonNull
    @Override
    public ProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsHolder holder, int position) {
        holder.setData(allProductList.get(position));
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(context);
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (user != null){
                    orderItemsSingleTone = OrderItemsSingleTone.newInstance();
                    orderItemListList = orderItemsSingleTone.getOrderItemList();
                    orderItemList = new OrderItemList();
                    orderItemList.setProduct_id(Integer.parseInt(allProductList.get(position).getProductId()));
                    orderItemList.setProduct_qty(Integer.parseInt(holder.txt_product_amount.getText().toString())+"");
                    orderItemList.setProduct_price(Double.parseDouble(allProductList.get(position).getProductPrice()));
                    orderItemList.setTotal_price(Double.parseDouble(allProductList.get(position).getProductPrice())*Integer.parseInt(holder.txt_product_amount.getText().toString()));
                    orderItemList.setProduct_img("http://jjeda.fpalatheer.xyz/uploads/product/"+allProductList.get(position).getImage());
                    orderItemList.setProduct_name(allProductList.get(position).getProductName());
                    Log.e("image",allProductList.get(position).getImage());
                    //orderItemsSingleTone.AddProduct(orderItemList);
                    if (!orderItemListList.isEmpty()){
                        for (int i = 0;i<orderItemListList.size();i++){
                            if(orderItemListList.get(i).getProduct_id()== orderItemList.getProduct_id() ){
                                orderItemsSingleTone.UpdateProduct(orderItemList);
                                // Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                            }else {
                                //Toast.makeText(context, "hello2", Toast.LENGTH_SHORT).show();
                                orderItemsSingleTone.AddProduct(orderItemList);
                            }
                        }
                    }else {
                        orderItemsSingleTone.AddProduct(orderItemList);
                    }
                    homeFragment.setBasketItems(orderItemListList.size());
                }else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    Toast.makeText(context, "برجاء تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                    homeActivity.finish();
                }*/
                homeFragment.sendData(allProductList.get(position));
            }
        });
        /*holder.minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(holder.txt_product_amount.getText().toString()) == 1){
                    holder.txt_product_amount.setText(1+"");
                    //total_price = count*size_price;
                }else {
                    //count = Integer.parseInt(holder.txt_product_amount.getText().toString())-1;
                    holder.txt_product_amount.setText(Integer.parseInt(holder.txt_product_amount.getText().toString())-1+"");
                }
                //count = 1;
            }
        });
        holder.add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //plus();
                holder.txt_product_amount.setText(Integer.parseInt(holder.txt_product_amount.getText().toString())+1+"");
                //count = 1;
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment.sendData(allProductList.get(position));
            }
        });
        /*holder.txt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment.sendData(allProductList.get(position));
            }
        });*/
    }
    @Override
    public int getItemCount() {
        return allProductList.size();
    }

    class ProductsHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.product_img)
        ImageView product_img;
        @BindView(R.id.offer_img)
        ImageView offer_img;
        @BindView(R.id.txt_product_name)
        TextView txt_product_name;
        @BindView(R.id.txt_details)
        ReadMoreTextView txt_details;
        @BindView(R.id.btn_add)
        Button btn_add;
        /*@BindView(R.id.img_add)
        ImageView add_img;
        @BindView(R.id.img_minus)
        ImageView minus_img;
        @BindView(R.id.txt_product_amount)
        TextView txt_product_amount;*/
        public ProductsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(AllProductsCity allProduct) {
            if (allProduct.getOfferd()==0){
                offer_img.setVisibility(View.GONE);
            }else {
                offer_img.setVisibility(View.VISIBLE);
            }
            Picasso.get().load("https://thabaeh.com/uploads/product/"+allProduct.getImage()).into(product_img);
            txt_product_name.setText(allProduct.getProductName());
            txt_details.setText(Html.fromHtml(allProduct.getProductDesc().trim()));
            //txt_product_amount.setText(count+"");
            //product_img.setImageResource(R.drawable.splash2);
        }

        /*public void sendData(AllProductsCity allProduct) {
            Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
            homeFragment.sendData(allProduct);
        }*/
    }
}
