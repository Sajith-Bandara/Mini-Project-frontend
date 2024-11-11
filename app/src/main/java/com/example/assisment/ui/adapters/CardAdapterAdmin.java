package com.example.assisment.ui.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assisment.R;
import com.example.assisment.ui.activities.OpenActivity;
import com.example.assisment.ui.activities.SubscribedActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CardAdapterAdmin extends RecyclerView.Adapter<CardAdapterAdmin.CardViewHolder> {
    private JSONArray cardItemList;

    public CardAdapterAdmin(JSONArray cardItemList) {
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public CardAdapterAdmin.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_admin, parent, false);
        return new CardAdapterAdmin.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapterAdmin.CardViewHolder holder, int position) {
        try {
            // Get the JSON object at the current position
            JSONObject jsonObject = cardItemList.getJSONObject(position);

            // Extract values from JSON object
            String title = jsonObject.optString("event_name");
            String description = jsonObject.optString("event_description");
            String count = jsonObject.optString("count");
            String eventId = jsonObject.optString("event_id");

            // Set values to TextViews
            holder.cardTitle.setText(title);
            holder.cardDescription.setText(description);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cardItemList.length();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle, cardDescription, cardStatus;
        View cardViewHolder;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            cardStatus = itemView.findViewById(R.id.card);
            cardViewHolder = itemView.findViewById(R.id.cardStatus);
        }
    }
}
