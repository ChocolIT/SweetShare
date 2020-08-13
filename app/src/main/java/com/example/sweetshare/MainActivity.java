package com.example.sweetshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new TabPagerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Profile");
                        tab.setIcon(R.drawable.profile_icon);
                        break;
                    case 1:
                        tab.setText("Explore");
                        tab.setIcon(R.drawable.explore_icon);
                        break;
                    case 2:
                        tab.setText("Messages");
                        tab.setIcon(R.drawable.messages_icon);
                        break;
                }
            }
        }
        );
        tabLayoutMediator.attach();
        viewPager2.setCurrentItem(1, false);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public Map getUserData() {
        Intent intent = getIntent();
        Map<String, Object> userData = new HashMap<>();

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        String userFullName = sharedPreferences.getString(UserConstants.USER_FULL_NAME, "Default Value Full Name");
        userData.put(UserConstants.USER_FULL_NAME, userFullName);

        String userEmail = sharedPreferences.getString(UserConstants.USER_EMAIL, "Default Value Email");
        userData.put(UserConstants.USER_EMAIL, userEmail);

        Long userReputation = sharedPreferences.getLong(UserConstants.USER_REPUTATION, 404);
        userData.put(UserConstants.USER_REPUTATION, userReputation);

        Boolean userHasCustomPhoto = sharedPreferences.getBoolean(UserConstants.USER_HAS_CUSTOM_PICTURE, false);
        userData.put(UserConstants.USER_HAS_CUSTOM_PICTURE, userHasCustomPhoto);

        String userProfileImgUri = sharedPreferences.getString(UserConstants.USER_PROFILE_PICTURE_URI, "Default Uri");
        userData.put(UserConstants.USER_PROFILE_PICTURE_URI, userProfileImgUri);

        return userData;
    }
}