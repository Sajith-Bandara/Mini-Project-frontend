package com.example.assisment.data.models;

public class RecoverDetais {

    private String fullName;
    private String dateOfBirth;
    private String mothersMaidenName;
    private String childhoodBestFriendName;
    private String childhoodPetName;
    private String ownQuestion;
    private String ownAnswer;

    public RecoverDetais(String fullName, String dateOfBirth, String mothersMaidenName, String childhoodBestFriendName, String childhoodPetName, String ownQuestion, String ownAnswer) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.mothersMaidenName = mothersMaidenName;
        this.childhoodBestFriendName = childhoodBestFriendName;
        this.childhoodPetName = childhoodPetName;
        this.ownQuestion = ownQuestion;
        this.ownAnswer = ownAnswer;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMothersMaidenName() {
        return mothersMaidenName;
    }

    public String getChildhoodBestFriendName() {
        return childhoodBestFriendName;
    }

    public String getChildhoodPetName() {
        return childhoodPetName;
    }

    public String getOwnQuestion() {
        return ownQuestion;
    }

    public String getOwnAnswer() {
        return ownAnswer;
    }
}
