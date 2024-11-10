package com.example.assisment.data.api;


import androidx.media3.common.C;

import com.example.assisment.data.models.ChangePassword;
import com.example.assisment.data.models.LoginRequest;
import com.example.assisment.data.models.LoginResponse;
import com.example.assisment.data.models.NormalResponse;
import com.example.assisment.data.models.RecoverDetais;
import com.example.assisment.data.models.RecoveryData;
import com.example.assisment.data.models.SignupRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    @GET("recovery/get")
    Call<RecoveryData> getRecoveryData(@Header("Authorization") String token);

    @GET("auth/mydetails")
    Call<Map<String,String>> getEmail(@Header("Authorization") String token);

    @POST("auth/changePassword")
    Call <Map<String,String>> changePassword(@Body ChangePassword req, @Header("Authorization") String token);
}

