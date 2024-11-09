package com.example.assisment.data.api;


import com.example.assisment.data.models.LoginRequest;
import com.example.assisment.data.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}

