package com.example.sweetshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            String uID = fAuth.getCurrentUser().getUid();
            Util.fetchUserDataFromFireStoreAndStartMainActivity(uID, SplashScreen.this);
        }
        else {
            Intent intent = new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}