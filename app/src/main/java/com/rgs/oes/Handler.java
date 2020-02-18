package com.rgs.oes;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Handler extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
