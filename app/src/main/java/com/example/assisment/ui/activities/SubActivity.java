package com.example.assisment.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assisment.R;
import com.example.assisment.data.api.ApiService;
import com.example.assisment.data.models.EventRequest;
import com.example.assisment.data.models.OnEventsReceived;
import com.example.assisment.data.models.SubEvents;
import com.example.assisment.ui.adapters.CardAdaptorSub;
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

public class SubActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "app_prefs";
    private static final String TOKEN_KEY = "token";
    private static final String PRIVATE_KEY = "private_key";

    private RecyclerView recyclerView;
    private CardAdaptorSub cardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            getSubEventCards(new OnEventsReceived() {
                @Override
                public void onReceived(JSONArray eventArray) {
                    cardAdapter = new CardAdaptorSub(eventArray);
                    recyclerView.setAdapter(cardAdapter);
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void  getSubEventCards(OnEventsReceived listener) throws JSONException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, "");

        EventRequest event = new EventRequest(sharedPreferences.getString(PRIVATE_KEY,""));
        apiService.getSubscribeEvent(event,token).enqueue(new Callback<List<SubEvents>>() {
            @Override
            public void onResponse(Call<List<SubEvents>> call, Response<List<SubEvents>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<SubEvents> eventList = response.body();

                    try {
                        JSONArray eventArray = new JSONArray();
                        Gson gson = new Gson();
                        for (SubEvents e : eventList) {
                            JSONObject json = new JSONObject();
                            json.put("dateOfBirth", e.getDateOfBirth());
                            json.put("timeOfBirth", e.getTimeOfBirth());
                            json.put("locationOfBirth", e.getLocationOfBirth());
                            json.put("bloodGroup", e.getBloodGroup());
                            json.put("sex", e.getSex());
                            json.put("height", e.getHeight());
                            json.put("ehanicity", e.getEhanicity());
                            json.put("eyeColour", e.getEyeColour());
                            json.put("useridencrypted", e.getUseridencrypted());

                            String eventJson = gson.toJson(e.getDataSourcingEvents());
                            json.put("dataSourcingEvents", eventJson); // Store as JSON string
                            eventArray.put(json);
                        }

                        // Notify the listener with the JSONArray
                        listener.onReceived(eventArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SubActivity.this, getString(R.string.event_retrieve_failed_toast), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SubEvents>> call, Throwable t) {
                Toast.makeText(SubActivity.this, getString(R.string.network_error_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }

}