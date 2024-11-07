package com.example.assisment;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private JSONArray cardItemList;

    public CardAdapter(JSONArray cardItemList) {
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        try {
            // Get the JSON object at the current position
            JSONObject jsonObject = cardItemList.getJSONObject(position);

            // Extract values from JSON object
            String title = jsonObject.optString("cardTitle");
            String description = jsonObject.optString("cardDescription");
            String status = jsonObject.optString("cardStatus");

            // Set values to TextViews
            holder.cardTitle.setText(title);
            holder.cardDescription.setText(description);
            holder.cardStatus.setText(status);

            if ("Open".equalsIgnoreCase(status)) {
                holder.cardStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
            } else if ("Close".equalsIgnoreCase(status)) {
                holder.cardStatus.setBackgroundColor(Color.parseColor("#FFFF9800"));
            } else if ("Subscribed".equalsIgnoreCase(status)) {
                holder.cardStatus.setBackgroundColor(Color.parseColor("#FF3F51B5"));
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent;
                if ("Open".equalsIgnoreCase(status)) {
                    intent = new Intent(v.getContext(), OpenActivity.class);
                } else if ("Subscribed".equalsIgnoreCase(status)) {
                    intent = new Intent(v.getContext(), SubscribedActivity.class);
                } else {
                    return;
                }
                v.getContext().startActivity(intent);
            });


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

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            cardStatus = itemView.findViewById(R.id.cardStatus);
        }
    }
}
