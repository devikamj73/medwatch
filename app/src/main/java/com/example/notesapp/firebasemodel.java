package com.example.notesapp;

public class firebasemodel {
    private String title;

    public firebasemodel() {
        // Default constructor required for Firestore
    }

    public firebasemodel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
