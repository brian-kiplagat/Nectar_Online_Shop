package com.nectar.nectaronline;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    Context context;
    public String email;

    public Preferences(Context context) {
        this.context = context;
    }

    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        return email;
    }
}
