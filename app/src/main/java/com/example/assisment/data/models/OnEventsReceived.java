package com.example.assisment.data.models;

import org.json.JSONArray;

public interface OnEventsReceived {
    void onReceived(JSONArray eventArray);
}