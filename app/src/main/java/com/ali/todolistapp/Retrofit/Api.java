package com.ali.todolistapp.Retrofit;

import com.ali.todolistapp.Models.DoList;
import com.ali.todolistapp.Models.Item;
import com.ali.todolistapp.Models.UserClass;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface Api {

    @FormUrlEncoded
    @POST("user_register")
    Call <UserClass> user_register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password
    );

    @FormUrlEncoded
    @POST("user_login")
    Call <UserClass> user_login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("get_lists")
    Call <List <DoList>> myLists(
            @Field("apiToken") String apiToken
    );

    @FormUrlEncoded
    @POST("add_list")
    Call <DoList> add_list(
            @Field("apiToken") String apiToken,
            @Field("list_name") String list_name

    );

    @FormUrlEncoded
    @POST("add_item")
    Call <Item> add_item(
            @Field("apiToken") String apiToken,
            @Field("list_id") String list_id,
            @Field("item_name") String item_name,
            @Field("item_desc") String item_desc,
            @Field("item_status") String item_status,
            @Field("deadline_date") String deadline_date,
            @Field("deadline_time") String deadline_time,
            @Field("expired") String expired
    );

    @FormUrlEncoded
    @POST("get_item_list")
    Call <List <Item>> get_item_list(
            @Field("apiToken") String apiToken,
            @Field("list_id") String list_id
    );

    @FormUrlEncoded
    @POST("item_delete")
    Call <Item> delete_item(
            @Field("apiToken") String apiToken,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("list_delete")
    Call <DoList> list_delete(
            @Field("apiToken") String apiToken,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("item_completed")
    Call <Item> item_completed(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("item_expired")
    Call <Item> item_expired(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("get_send_to_list")
    Call <List <Item>> get_send_to_list(
            @Field("apiToken") String apiToken,
            @Field("list_id") String list_id
    );


}