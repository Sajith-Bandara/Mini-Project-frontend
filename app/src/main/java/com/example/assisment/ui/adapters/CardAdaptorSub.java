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
import com.example.assisment.data.models.Event;
import com.example.assisment.ui.activities.SubscribedActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CardAdaptorSub extends RecyclerView.Adapter<CardAdaptorSub.CardViewHolder>{

    private JSONArray cardItemList;


    public CardAdaptorSub(JSONArray cardItemList) {
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public CardAdaptorSub.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardAdaptorSub.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdaptorSub.CardViewHolder holder, int position) {
        try {

            JSONObject jsonObject = cardItemList.getJSONObject(position);
            String eventJson = jsonObject.optString("dataSourcingEvents");
            Gson gson = new Gson();

            Event event = gson.fromJson(eventJson, Event.class);
            String title = event.getEvent_name();
            String description = event.getEvent_description();
            String status = "Subscribed";
            int count = event.getCount();
            String eventId = String.valueOf(event.getEvent_id());
            Log.i("filter","eventId "+eventId);

            holder.cardTitle.setText(title);
            holder.cardDescription.setText(description);
            holder.cardStatus.setText(status);
            holder.cardViewHolder.setBackgroundColor(Color.parseColor("#FF3F51B5"));


            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SubscribedActivity.class);
                intent.putExtra("eventId",eventId);
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
