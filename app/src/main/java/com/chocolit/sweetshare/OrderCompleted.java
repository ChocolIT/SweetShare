package com.chocolit.sweetshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderCompleted extends AppCompatActivity {

    private TextView ownerName, ownerPhone, reservationDate;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);

        ownerName = findViewById(R.id.ownerName);
        reservationDate = findViewById(R.id.reservationDate);
        ownerPhone = findViewById(R.id.ownerPhone);
        continueButton = findViewById(R.id.continueButton);

        Bundle data = getIntent().getExtras();

        ownerName.setText(data.getString(UserConstants.USER_FULL_NAME));
        String date = data.getString(OrderConstants.START_DATE) + "  -  " + data.getString(OrderConstants.END_DATE);
        reservationDate.setText(date);
        ownerPhone.setText(data.getString(ProductConstants.PRODUCT_OWNER_PHONE_NUMBER));

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}