package com.example.assisment.data.models;

public class SubEvents {

    private String id;
    private String dateOfBirth;
    private String timeOfBirth;
    private String locationOfBirth;
    private String bloodGroup;
    private String sex;
    private String height;
    private String ehanicity;
    private String eyeColour;
    private String useridencrypted;
    private Event dataSourcingEvents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public String getLocationOfBirth() {
        return locationOfBirth;
    }

    public void setLocationOfBirth(String locationOfBirth) {
        this.locationOfBirth = locationOfBirth;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEhanicity() {
        return ehanicity;
    }

    public void setEhanicity(String ehanicity) {
        this.ehanicity = ehanicity;
    }

    public String getEyeColour() {
        return eyeColour;
    }

    public void setEyeColour(String eyeColour) {
        this.eyeColour = eyeColour;
    }

    public String getUseridencrypted() {
        return useridencrypted;
    }

    public void setUseridencrypted(String useridencrypted) {
        this.useridencrypted = useridencrypted;
    }

    public Event getDataSourcingEvents() {
        return dataSourcingEvents;
    }

    public void setDataSourcingEvents(Event event) {
        this.dataSourcingEvents = event;
    }
}
