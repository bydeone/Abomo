package com.deone.abomo.outils;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;

import android.content.Context;

import com.deone.abomo.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DbUser {
    private DatabaseReference reference;
    private Context context;
    private User user;

    public DbUser(Context context, User user) {
        this.context = context;
        this.user = user;
        this.reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
