package com.example.assisment.ui.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assisment.R;
import com.example.assisment.ui.adapters.CardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JSONArray cardItemList = getJsonArray();

        cardAdapter = new CardAdapter(cardItemList);

        recyclerView.setAdapter(cardAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private static @NonNull JSONArray getJsonArray() {
        JSONArray cardItemList = new JSONArray();
        JSONObject jsonObject3 = new JSONObject();
        try {
            jsonObject3.put("cardTitle", "value3");
            jsonObject3.put("cardDescription", "value4");
            jsonObject3.put("cardStatus", "Subscribed");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        cardItemList.put(jsonObject3);
        return cardItemList;
    }
}