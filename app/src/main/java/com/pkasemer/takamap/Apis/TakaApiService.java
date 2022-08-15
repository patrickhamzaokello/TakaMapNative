package com.pkasemer.takamap.Apis;

import com.pkasemer.takamap.Models.HomeFeed;
import com.pkasemer.takamap.Models.PickupResponse;
import com.pkasemer.takamap.Models.RequestPickupModel;
import com.pkasemer.takamap.Models.UserOrders;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface TakaApiService {


    //    get all infrastructure
//    http://localhost:8080/projects/TakaMap/mobile/api/v1/infrastructure/read.php?page=1
//    @GET("read.php")
//    Call<HomeFeed> getAllInfrastructure(
//            @Query("page") int pageIndex
//    );

    @GET("near_me.php")
    Call<HomeFeed> getAllInfrastructure(
            @Query("page") int pageIndex
    );

    //post Address
    @POST("request_pickup.php")
    @Headers("Cache-Control: no-cache")
    Call<PickupResponse> postCreatePickupRequest(
            @Body RequestPickupModel requestPickupModel
    );


    //fetch past orders
    @GET("orders/userOrders.php")
    Call<UserOrders> getUserOrders(
            @Query("customerId") int customerID,
            @Query("page") int pageIndex
    );


}