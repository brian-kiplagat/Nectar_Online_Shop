package com.nectar.nectaronline;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public String getEmail() {
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        return preferences.getString("email", "");
    }

    public String getToken() {
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        return preferences.getString("token", "");
    }
    public String getPassword() {
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        return preferences.getString("password", "");
    }

    public String getName() {
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        return preferences.getString("name", "");
    }

    public String getNumber() {
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        return preferences.getString("number", "");
    }

    public String getAddress() {
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        return preferences.getString("address", "");
    }

    public String getDescription() {
        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        return preferences.getString("desc", "");
    }

}
