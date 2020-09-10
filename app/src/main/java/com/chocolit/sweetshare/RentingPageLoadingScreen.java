package com.chocolit.sweetshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RentingPageLoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renting_page_loading_screen);
        String productID = getIntent().getExtras().getString(ProductConstants.ID);

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference productDocument = fStore.collection("products").document(productID);
        productDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> disabledDatesList = (ArrayList<String>) documentSnapshot.get(ProductConstants.DISABLED_DATES_LIST);
                Intent intent = new Intent(getApplicationContext(), ProductReservation.class);
                intent.putStringArrayListExtra(ProductConstants.DISABLED_DATES_LIST, disabledDatesList);
                intent.putExtra(ProductConstants.ID, productID);
                intent.putExtra(UserConstants.USER_ID, documentSnapshot.getString(UserConstants.USER_ID));
                startActivity(intent);
                finish();
            }
        });
    }
}