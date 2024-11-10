package com.example.assisment.data.api;


import androidx.media3.common.C;

import com.example.assisment.data.models.ChangePassword;
import com.example.assisment.data.models.Event;
import com.example.assisment.data.models.EventRequest;
import com.example.assisment.data.models.LoginRequest;
import com.example.assisment.data.models.LoginResponse;
import com.example.assisment.data.models.NormalResponse;
import com.example.assisment.data.models.RecoverDetais;
import com.example.assisment.data.models.RecoveryData;
import com.example.assisment.data.models.ResetPW;
import com.example.assisment.data.models.SignupRequest;
import com.example.assisment.data.models.SubEvents;
import com.example.assisment.data.models.SubscribeEvent;

import org.json.JSONObject;

import java.util.List;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
    @GET("recovery/get")
    Call<RecoveryData> getRecoveryData(@Header("Authorization") String token);

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

    @POST("userdetails/getMySubscribedData")
    Call<List<SubEvents>>  getSubscribeEvent(
            @Body EventRequest event,
            @Header("Authorization") String token
    );
    @GET("userdetails/get_using_id")
    Call<SubEvents>  getSubEvent(
            @Query("id") String eventId,
            @Header("Authorization") String token
    );
    @HTTP(method = "DELETE", path = "userdetails/delete", hasBody = true)
    Call<NormalResponse>  deleteSubEvent(
            @Query("id") String eventId,
            @Body EventRequest event,
            @Header("Authorization") String token
    );

    @GET("auth/mydetails")
    Call<Map<String,String>> getEmail(@Header("Authorization") String token);

    @POST("auth/changePassword")
    Call <Map<String,String>> changePassword(@Body ChangePassword req, @Header("Authorization") String token);

    @POST("auth/recover_account")
    Call<Map<String,String>> recoverAccount(@Body RecoveryData recoverAccount);

    @POST("auth/resetPassword")
    Call<Map<String,String>> resetPassword(@Body ResetPW resetPassword);

}

