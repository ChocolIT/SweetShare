package com.example.sweetshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent intent = getIntent();
        Map<String, Object> userData = (HashMap<String, Object>)intent.getSerializableExtra(Constants.USER_DATA);
        String user_name = userData.get(Constants.USER_FULL_NAME).toString();

        TextView text = findViewById(R.id.text1);
        text.setText(user_name);
    }
}