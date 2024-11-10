package com.example.assisment.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assisment.R;
import com.example.assisment.data.api.ApiService;
import com.example.assisment.data.models.NormalResponse;
import com.example.assisment.data.models.RecoverDetais;
import com.example.assisment.ui.adapters.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecoverFormActivity2 extends AppCompatActivity {

    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";

    private EditText question;
    private EditText answer;
    private Button button;
    private final String tag = "filter";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recover_form2);

        question = findViewById(R.id.ownQuestion);
        answer = findViewById(R.id.ownAnswer);
        button = findViewById(R.id.continueBtn);

        Intent foreignIntent = getIntent();

        String name = foreignIntent.getStringExtra("name");
        String dob = foreignIntent.getStringExtra("dob");
        String fName = foreignIntent.getStringExtra("fName");
        String mName = foreignIntent.getStringExtra("mName");
        String pName = foreignIntent.getStringExtra("pName");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question.getText().toString().isEmpty() || answer.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.empty_field_toast),Toast.LENGTH_LONG).show();
                }else{
                    RecoverDetais recoverDetais = new RecoverDetais(name,dob,mName,fName,pName,question.getText().toString(),answer.getText().toString());
                    sendRecoverDetails(recoverDetais);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void sendRecoverDetails(RecoverDetais recoverDetais) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, "");

        Intent homeIntent = new Intent(this,HomeActivity.class);

        apiService.recoverDetailsSave(recoverDetais,  token).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {

                Log.i(tag,"massage "+response.code());
                if (response.isSuccessful()) {
                    startActivity(homeIntent);
                } else {
                    Toast.makeText(RecoverFormActivity2.this, getString(R.string.data_save_failed_toast), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Log.e(tag,t.toString());
                Toast.makeText(RecoverFormActivity2.this, getString(R.string.network_error_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }
}