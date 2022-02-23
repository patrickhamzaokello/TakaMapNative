package com.pkasemer.takamap.Apis;

import com.pkasemer.takamap.Models.HomeFeed;
import com.pkasemer.takamap.Models.UserOrders;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MovieService {


//    get all infrastructure
//    http://localhost:8080/projects/TakaMap/mobile/api/v1/infrastructure/read.php?page=1
    @GET("infrastructure/read.php")
    Call<HomeFeed> getAllInfrastructure(
            @Query("page") int pageIndex
    );


    //fetch past orders
    @GET("orders/userOrders.php")
    Call<UserOrders> getUserOrders(
            @Query("customerId") int customerID,
            @Query("page") int pageIndex
    );


}