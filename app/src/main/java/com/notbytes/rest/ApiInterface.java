package com.notbytes.rest;

import com.notbytes.model.ProductAddResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;



public interface ApiInterface
{

    @FormUrlEncoded
    @POST("AddLeftRightDeviceId")
    Call<ProductAddResponse> getproductresponse
            (

                    @Field("productId") String productId,
                    @Field("leftDeviceId") String leftDeviceId,
                    @Field("rightDeviceId") String rightDeviceId


            );
}
