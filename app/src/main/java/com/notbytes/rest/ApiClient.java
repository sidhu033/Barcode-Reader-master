package com.notbytes.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    public static final String BASE_URL = "http://192.168.1.9/GamRedoxer/api/product/";
    private static Retrofit retrofit = null;






  private static OkHttpClient okClient()
    {
        return new OkHttpClient.Builder()

                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit getClient()
    {
        if (retrofit== null)
        {
            retrofit = new Retrofit.Builder()

                    .baseUrl(BASE_URL)
                     .client(ApiClient.okClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
