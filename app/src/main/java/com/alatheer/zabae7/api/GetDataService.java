package com.alatheer.zabae7.api;

import com.alatheer.zabae7.forgetpassword.ForgetPasswordModel;
import com.alatheer.zabae7.editpassword.SuccessModel;
import com.alatheer.zabae7.home.basket.BasketModel;
import com.alatheer.zabae7.home.basket.OrderSuccess;
import com.alatheer.zabae7.home.home2.ProductModel;
import com.alatheer.zabae7.home.messaging.MessageModel;
import com.alatheer.zabae7.home.messaging.SuccessMessageModel;
import com.alatheer.zabae7.home.orders.OrderModel;
import com.alatheer.zabae7.home.profile.EditProfile;
import com.alatheer.zabae7.home.profile.UserModel;
import com.alatheer.zabae7.home.about.AboutModel;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;
import com.alatheer.zabae7.payment.Bank;
import com.alatheer.zabae7.signup.CityModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface GetDataService {

    @Multipart
    @POST("Api/register")
    Call<User> signup_user_with_img(@Part("user_name")RequestBody user_name,
                                  @Part("user_phone")RequestBody user_phone,
                                  @Part("user_email")RequestBody user_email,
                                  @Part("user_city")RequestBody user_city,
                                  @Part("user_pass")RequestBody user_pass,
                                  @Part("user_adress") RequestBody user_adress,
                                  @Part MultipartBody.Part user_img);
    @FormUrlEncoded
    @POST("Api/register")
    Call<User> signup_user(@Field("user_name")String user_name,
                                  @Field("user_phone")String user_phone,
                                  @Field("user_email")String user_email,
                                  @Field("user_city")String user_city,
                                  @Field("user_pass")String user_pass,
                                  @Field("user_adress") String user_adress);
    @POST("Api/getAllcities")
    Call<CityModel> getCities();
    @FormUrlEncoded
    @POST("Api/login_app")
    Call<User> login_User(@Field("user_phone")String user_phone,@Field("user_pass")String user_pass);

    @FormUrlEncoded
    @POST("Api/ForgetPass")
    Call<ForgetPasswordModel> forgetpassword(@Field("user_email")String user_email);

    @FormUrlEncoded
    @POST("Api/getProfile")
    Call<UserModel> get_profile(@Field("user_id")String user_id);

    @FormUrlEncoded
    @POST("Api/edit_profile")
    Call<EditProfile> edit_profile_without_image(@Field("user_id")String user_id,
                                   @Field("user_pass")String user_pass,
                                   @Field("user_name")String user_name,
                                   @Field("user_email")String user_email,
                                   @Field("user_phone")String user_phone,
                                   @Field("user_city")String user_city);
    @Multipart
    @POST("Api/edit_profile")
    Call<EditProfile> edit_profile_with_image(@Part("user_id") RequestBody user_id,
                                              @Part("user_pass")RequestBody user_pass,
                                              @Part("user_name")RequestBody user_name,
                                              @Part("user_email")RequestBody user_email,
                                              @Part("user_phone")RequestBody user_phone,
                                              @Part("user_city")RequestBody user_city,
                                              @Part MultipartBody.Part img);

    @GET("Api/get_all_offers/{id}")
    Call<ProductModel> get_all_offers(@Path("id")String id);

    @GET("Api/getAllproducts/{id}")
    Call<ProductModel> get_all_products(@Path("id")String id);

    @POST("Api/saveStoreOrders")
    Call<OrderSuccess> send_order(@Body BasketModel basketModel);

    @FormUrlEncoded
    @POST("Api/customer_product_orders")
    Call<OrderModel> get_orders(@Field("customer_id") String id, @Field("type")String type);

    @FormUrlEncoded
    @POST("Api/send_message")
    Call<SuccessMessageModel> sendMessage(@Field("user_id") String id,
                                          @Field("user_name")String user_name,
                                          @Field("user_phone")String user_phone,
                                          @Field("msg_title")String msg_title,
                                          @Field("msg_content")String msg_content);
    @GET("Api/MyMsg/{id}")
    Call<List<MessageModel>> get_messages(@Path("id") String id);

    @GET("Api/getAppinfo")
    Call<AboutModel> get_app_info();

    @FormUrlEncoded
    @POST("Api/get_notification")
    Call<NotificationModel> get_notifications(@Field("user_id")String user_id);

    @FormUrlEncoded
    @POST("Api/get_product_details")
    Call<com.alatheer.zabae7.updateproduct.ProductModel> get_product_details(@Field("product_id")Integer product_id);

    @FormUrlEncoded
    @POST("Api/update_password")
    Call<SuccessModel> update_password(@Field("user_id")String user_id,
                                       @Field("old_password")String old_password,
                                       @Field("new_password")String new_password);
    @FormUrlEncoded
    @POST("Api/check_cobone")
    Call<SuccessModel> check_copon(@Field("cobone")String cobone);

    @FormUrlEncoded
    @POST("Api/update_token")
    Call<SuccessModel> update_token(@Field("user_id")String user_id,@Field("token")String cobone);

    @FormUrlEncoded
    @POST("Api/cancel_order")
    Call<SuccessModel> cancel_order(@Field("order_id")String order_id,
                                    @Field("user_id")String user_id,
                                    @Field("reason")String reason);
    @GET("Api/get_transfer_bank")
    Call<Bank> get_bank_data();

    @FormUrlEncoded
    @POST("Api/get_order_details")
    Call<com.alatheer.zabae7.notificationdata.OrderModel> get_order_details(@Field("order_id")String order_id);
}
