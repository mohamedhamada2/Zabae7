package com.alatheer.zabae7.data;

import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.singleton.OrderItemsSingleTone;

import java.util.List;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import retrofit2.http.DELETE;

@androidx.room.Dao
public interface Dao {

    @Insert
    void AddProduct(OrderItemList orderItemList);

    @Query("select * from PRODUCT")
    List<OrderItemList> getallproducts();

    @Query("delete from PRODUCT")

    void deleteAllproduct();

    @Query("delete from PRODUCT where product_id =:id AND size_id=:id2")
    void DeleteProduct(int id,int id2);
    @Update
    void editproduct(OrderItemList basketModel);
}
