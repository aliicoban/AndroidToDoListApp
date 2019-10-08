package com.ali.todolistapp.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aliç on 29.9.2018.
 */

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2/";//local
    private static RetrofitClient mIntance;
    private static Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized RetrofitClient getIntance(){

        if(mIntance==null){
            mIntance = new RetrofitClient();
        }
        return mIntance;

    }
    public Api getApi(){
        return retrofit.create(Api.class);
    }


}
