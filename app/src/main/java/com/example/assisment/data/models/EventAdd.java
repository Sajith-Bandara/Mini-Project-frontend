package com.example.assisment.data.models;

public class EventAdd {
    private String event_name;
    private  String event_description;

    public EventAdd(String event_name, String event_description) {
        this.event_name = event_name;
        this.event_description = event_description;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_description() {
        return event_description;
    }
}
