package com.example.assisment.ui.activities;

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
import com.example.assisment.data.models.EventAdd;
import com.example.assisment.data.models.NormalResponse;
import com.example.assisment.data.models.SubscribeEvent;
import com.example.assisment.ui.adapters.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventAddActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";

    Button button;
    EditText name,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_add);

        button = findViewById(R.id.eventSubmit);
        name = findViewById(R.id.eventName);
        description = findViewById(R.id.eventDescription);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty() || description.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.empty_field_toast),Toast.LENGTH_LONG).show();
                }else{
                    EventAdd showConfirmationDialog = new EventAdd(name.getText().toString(),description.getText().toString());
                    showConfirmationDialog(showConfirmationDialog);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showConfirmationDialog(EventAdd eventAdd) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.save_data_title))
                .setMessage(getString(R.string.conform_massage_save))
                .setPositiveButton(getString(R.string.button_Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendSubscribeData(eventAdd);
                    }
                })
                .setNegativeButton(R.string.button_No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
    }

    private void sendSubscribeData(EventAdd eventAdd) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, "");

        Intent home = new Intent(this,AdminHome.class);

        apiService.saveEvent(eventAdd,  token).enqueue(new Callback<NormalResponse>() {
            @Override
            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                Log.i("filter","cod "+response.code());
                if (response.isSuccessful()) {
                    startActivity(home);
                    finish();
                } else {
                    Toast.makeText(EventAddActivity.this, getString(R.string.data_save_failed_toast), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalResponse> call, Throwable t) {
                Toast.makeText(EventAddActivity.this, getString(R.string.network_error_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }

}