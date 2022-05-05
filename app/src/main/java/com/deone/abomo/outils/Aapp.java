package com.deone.abomo.outils;

import android.app.Application;

public class Aapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
