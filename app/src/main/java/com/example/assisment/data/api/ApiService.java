package com.example.assisment.data.api;


import com.example.assisment.data.models.Event;
import com.example.assisment.data.models.EventRequest;
import com.example.assisment.data.models.LoginRequest;
import com.example.assisment.data.models.LoginResponse;
import com.example.assisment.data.models.NormalResponse;
import com.example.assisment.data.models.RecoverDetais;
import com.example.assisment.data.models.SignupRequest;
import com.example.assisment.data.models.SubscribeEvent;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<LoginResponse> signup(@Body SignupRequest signupRequest);

    @POST("recovery/save")
    Call<NormalResponse> recoverDetailsSave(
            @Body RecoverDetais recoverDetais,
            @Header("Authorization") String token
    );

    @POST("sourcingEvent/getEvents")
    Call<List<Event>>  getEvents(
            @Body EventRequest event,
            @Header("Authorization") String token
    );

    @POST("userdetails/save")
    Call<NormalResponse>  subscribeEvent(
            @Body SubscribeEvent event,
            @Header("Authorization") String token
    );

}

