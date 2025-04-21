package com.example.meplusplus.context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DbContext {

    private static DatabaseReference reference;
    private static FirebaseDatabase database;

    private static DbContext instance = null;

    public DbContext() {
        if (database == null) {
            database = FirebaseDatabase.getInstance("https://meplusplus-c1c2e-default-rtdb.europe-west1.firebasedatabase.app/");
        }
    }

    public static synchronized DbContext getInstance() {
        if (instance == null) {
            instance = new DbContext();
        }
        return instance;
    }

    public DatabaseReference getReference(String name) {
        if (database == null) {
            throw new RuntimeException("Database is null");
        }
        return database.getReference(name);
    }

    public DatabaseReference getReference() {
        if (database == null) {
            throw new RuntimeException("Database is null");
        }
        return database.getReference();
    }

    public DatabaseReference getReferenceChild(String name) {
        if (database == null) {
            throw new RuntimeException("Database is null");
        }
        return database.getReference().child(name);
    }
}
