package com.chocolit.sweetshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        ServicesHelper.fetchData(SplashScreen.this);

        if (fAuth.getCurrentUser() != null) {
            String uID = fAuth.getCurrentUser().getUid();
            ServicesHelper.fetchUserDataFromFireStoreAndStartMainActivity(uID, SplashScreen.this);
        }
        else {
            Intent intent = new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}