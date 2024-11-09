package com.example.assisment.data.models;

public class SubscribeEvent {
    private String dateOfBirth;
    private String timeOfBirth;
    private String bloodGroup;
    private String sex;
    private String height;
    private String ehanicity;
    private String locationOfBirth;
    private String eyeColour;
    private String private_key;
    private String event_id;

    public SubscribeEvent(String dateOfBirth, String timeOfBirth, String bloodGroup, String sex,
                          String height, String ehanicity, String locationOfBirth, String eyeColour,
                          String private_key, String event_id) {
        this.dateOfBirth = dateOfBirth;
        this.timeOfBirth = timeOfBirth;
        this.bloodGroup = bloodGroup;
        this.sex = sex;
        this.height = height;
        this.ehanicity = ehanicity;
        this.locationOfBirth = locationOfBirth;
        this.eyeColour = eyeColour;
        this.private_key = private_key;
        this.event_id = event_id;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getSex() {
        return sex;
    }

    public String getHeight() {
        return height;
    }

    public String getEhanicity() {
        return ehanicity;
    }

    public String getLocationOfBirth() {
        return locationOfBirth;
    }

    public String getEyeColour() {
        return eyeColour;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public String getEvent_id() {
        return event_id;
    }
}
