package com.example.assisment.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.assisment.data.models.SubscribeEvent;
import com.example.assisment.ui.adapters.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String PRIVATE_KEY = "private_key";
    private Button button;
    EditText dob,tob,bloodGroup,sex,height,ethnicity,eyeColour,location;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_open);

        dob = findViewById(R.id.dobOpen);
        tob = findViewById(R.id.tobOpen);
        bloodGroup = findViewById(R.id.bloodGroupOpen);
        sex = findViewById(R.id.sexOpen);
        height = findViewById(R.id.heightOpen);
        ethnicity = findViewById(R.id.ethnicityOpen);
        eyeColour = findViewById(R.id.eyeColorOpen);
        location = findViewById(R.id.locationOpen);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String privateKey = sharedPreferences.getString(PRIVATE_KEY, "");

        Intent homeIntent = getIntent();
        String eventId = homeIntent.getStringExtra("event_id");
        button = findViewById(R.id.submitOpen);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dob.getText().toString().isEmpty() || tob.getText().toString().isEmpty() || bloodGroup.getText().toString().isEmpty()
                        || sex.getText().toString().isEmpty() || height.getText().toString().isEmpty() || ethnicity.getText().toString().isEmpty()
                        || eyeColour.getText().toString().isEmpty() || location.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.empty_field_toast),Toast.LENGTH_LONG).show();
                }else{

                    SubscribeEvent subscribeEvent = new SubscribeEvent(dob.getText().toString(),tob.getText().toString(),bloodGroup.getText().toString(),sex.getText().toString(),
                            height.getText().toString(),ethnicity.getText().toString(),location.getText().toString(),eyeColour.getText().toString(),privateKey,eventId);

                    showConfirmationDialog(subscribeEvent);
                }

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showConfirmationDialog(SubscribeEvent subscribeEvent) {
        new AlertDialog.Builder(this)
                .setTitle("Save Data")
                .setMessage("Do you want to save this information before proceeding?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendSubscribeData(subscribeEvent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
    }

    private void sendSubscribeData(SubscribeEvent subscribeEvent) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, "");

        Intent homeIntent = new Intent(this,HomeActivity.class);

        apiService.subscribeEvent(subscribeEvent,  token).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                Log.i("filter", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    startActivity(homeIntent);
                } else {
                    Toast.makeText(OpenActivity.this, "Data saving Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Toast.makeText(OpenActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Unsaved Data")
                .setMessage("You have unsaved data. Are you sure you want to go back?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Call super.onBackPressed only if the user confirms
                    OpenActivity.super.onBackPressed();
                })
                .setNegativeButton("No", null)
                .show();
    }

}