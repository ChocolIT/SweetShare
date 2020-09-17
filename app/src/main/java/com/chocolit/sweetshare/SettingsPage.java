package com.chocolit.sweetshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SettingsPage extends AppCompatActivity {

    private View aboutUsButton, faqButton, facebookButton, instagramButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        aboutUsButton = findViewById(R.id.aboutUsClickable);
        faqButton = findViewById(R.id.faqClickable);
        facebookButton = findViewById(R.id.facebookClickable);
        instagramButton = findViewById(R.id.instagramClickable);
        logoutButton = findViewById(R.id.logoutClickable);


        aboutUsButton.setOnClickListener(view -> {

        });
        faqButton.setOnClickListener(view -> {

        });
        facebookButton.setOnClickListener(view -> {

        });
        instagramButton.setOnClickListener(view -> {

        });
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();
            openSettingsSubpage(Login.class);
            finish();
        });
    }

    private void openSettingsSubpage(Class destination) {
        Intent intent = new Intent(this, destination);
        startActivity(intent);
    }
}