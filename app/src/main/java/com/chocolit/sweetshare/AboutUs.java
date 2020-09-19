package com.chocolit.sweetshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ImageView aboutUsPhoto = findViewById(R.id.aboutUsPhoto);
        ImageView icBackArrow = findViewById(R.id.icBackArrow);

        Picasso
                .get()
                .load(R.drawable.about_us_photo)
                .fit()
                .centerCrop()
                .into(aboutUsPhoto);

        icBackArrow.setOnClickListener((view) -> {
            finish();
        });
    }
}