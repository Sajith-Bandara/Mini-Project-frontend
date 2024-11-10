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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assisment.R;
import com.example.assisment.data.api.ApiService;
import com.example.assisment.data.models.EventRequest;
import com.example.assisment.data.models.OnEventsReceived;
import com.example.assisment.data.models.SubEvents;
import com.example.assisment.ui.adapters.Config;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscribedActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String PRIVATE_KEY = "private_key";
    private Button button;
    private TextView dob,tob,blood,sex,height,ethnicity,eyeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subscribed);

        dob = findViewById(R.id.dobText);
        tob = findViewById(R.id.tobText);
        blood = findViewById(R.id.bloodText);
        sex = findViewById(R.id.sexText);
        height = findViewById(R.id.heightText);
        ethnicity = findViewById(R.id.ethnicityText);
        eyeColor = findViewById(R.id.eyeColorText);

        Intent Eventintent = getIntent();
        String eventId = Eventintent.getStringExtra("eventId");
        Log.i("filter","event "+eventId);
        getSubEventDetails(eventId);

        button = findViewById(R.id.deleteSub);
        Intent intent = new Intent(this,HomeActivity.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void  getSubEventDetails(String eventId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, "");

        apiService.getSubEvent(eventId,token).enqueue(new Callback<SubEvents>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<SubEvents> call, Response<SubEvents> response) {

               if(response.isSuccessful() && response.body() != null){
                   dob.setText(response.body().getDateOfBirth());
                   tob.setText(response.body().getTimeOfBirth());
                   blood.setText(response.body().getBloodGroup());
                   sex.setText(response.body().getSex());
                   height.setText(response.body().getHeight());
                   ethnicity.setText(response.body().getEhanicity());
                   eyeColor.setText(response.body().getEyeColour());
                } else {
                    Toast.makeText(SubscribedActivity.this, getString(R.string.event_retrieve_failed_toast), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubEvents> call, Throwable t) {
                Toast.makeText(SubscribedActivity.this, getString(R.string.network_error_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }
}