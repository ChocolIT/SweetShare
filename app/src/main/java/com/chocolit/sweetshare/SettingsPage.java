package com.chocolit.sweetshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
            openSettingsSubpage(AboutUs.class);
        });
        faqButton.setOnClickListener(view -> {
            openSettingsSubpage(FaqPage.class);
        });
        facebookButton.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://www.facebook.com/SweetShareApp");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        instagramButton.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://www.instagram.com/sweetshareapp/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();
            openSettingsSubpage(Login.class);
            finish();
        });
        findViewById(R.id.icBackArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openSettingsSubpage(Class destination) {
        Intent intent = new Intent(this, destination);
        startActivity(intent);
    }
}