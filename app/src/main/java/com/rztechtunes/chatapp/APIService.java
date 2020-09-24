package com.rztechtunes.chatapp;


import com.rztechtunes.chatapp.Notification.MyResponse;
import com.rztechtunes.chatapp.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAk7MOXQs:APA91bGxlRlIUlQj_I-LDwD1uApbWeMnim0SEyF_K2LQXgtGij0bSzlheavFX0QJHR9_Z_Zsc5JdtMy_ODYMSe-KbgRuUttVm81MLct_7xezD_Ke-3t6h3upTdDMHsfJ2FbyPGvQ5OuM"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
