package com.example.assisment.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assisment.R;
import com.example.assisment.data.api.ApiService;
import com.example.assisment.data.models.Event;
import com.example.assisment.data.models.EventRequest;
import com.example.assisment.data.models.NormalResponse;
import com.example.assisment.data.models.OnEventsReceived;
import com.example.assisment.data.models.RecoverDetais;
import com.example.assisment.ui.adapters.CardAdapter;
import com.example.assisment.ui.adapters.Config;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String PRIVATE_KEY = "private_key";

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private View buttonProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        buttonProfile = findViewById(R.id.navigation_profile);

        Intent intent = new Intent(this,ProfileActivity.class);

        buttonProfile.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onResume() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            getEventCards(new OnEventsReceived() {
                @Override
                public void onReceived(JSONArray eventArray) {
                    cardAdapter = new CardAdapter(eventArray);
                    recyclerView.setAdapter(cardAdapter);
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        super.onResume();
    }

    private void  getEventCards(OnEventsReceived listener) throws JSONException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, "");

        EventRequest event = new EventRequest(sharedPreferences.getString(PRIVATE_KEY,""));
        apiService.getEvents(event,token).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Event> eventList = response.body();

                    try {
                        JSONArray eventArray = new JSONArray();
                        for (Event e : eventList) {
                            JSONObject json = new JSONObject();
                            json.put("event_name", e.getEvent_name());
                            json.put("event_description", e.getEvent_description());
                            json.put("tag", e.getTag());
                            json.put("count", e.getCount());
                            json.put("event_id", e.getEvent_id());
                            eventArray.put(json);
                        }

                        // Notify the listener with the JSONArray
                        listener.onReceived(eventArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(HomeActivity.this, "Failed to retrieve events", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit_title))
                .setMessage(getString(R.string.conform_massage_exit))
                .setPositiveButton(getString(R.string.button_Yes), (dialog, which) -> {
                    super.onBackPressed();
                    finishAffinity();
                    System.exit(0);
                })
                .setNegativeButton(getString(R.string.button_No), null)
                .show();
    }

}