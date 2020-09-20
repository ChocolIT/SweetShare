package com.chocolit.sweetshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class ProductLoadingScreen extends AppCompatActivity {

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_loading_screen);

        final String productID = getIntent().getExtras().getString(ProductConstants.ID);

        fStore = FirebaseFirestore.getInstance();

        final DocumentReference userDoc = fStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DocumentReference productDoc = fStore.collection("products").document(productID);

        final Intent intent = new Intent(this, ProductPage.class);

        productDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> productImageList = (ArrayList<String>) documentSnapshot.get(ProductConstants.PRODUCT_IMG_LIST);

                intent.putStringArrayListExtra(ProductConstants.PRODUCT_IMG_LIST, productImageList);

                intent.putExtra(ProductConstants.PRODUCT_TITLE, documentSnapshot.getString(ProductConstants.PRODUCT_TITLE));
                intent.putExtra(ProductConstants.PRODUCT_CITY, documentSnapshot.getString(ProductConstants.PRODUCT_CITY));
                intent.putExtra(ProductConstants.REVIEWS_NO, String.format("%s reviews", documentSnapshot.getLong(ProductConstants.REVIEWS_NO)));
                intent.putExtra(ProductConstants.PRICE, String.format("%s SWEETS/DAY", documentSnapshot.getLong(ProductConstants.PRICE)));
                intent.putExtra(ProductConstants.PRODUCT_DESCRIPTION, documentSnapshot.getString(ProductConstants.PRODUCT_DESCRIPTION));
                intent.putExtra(ProductConstants.PRODUCT_RATING, documentSnapshot.getLong(ProductConstants.PRODUCT_RATING));

                intent.putExtra(ProductConstants.ID, productID);

                userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> userFavorites = (List<String>) documentSnapshot.get(UserConstants.USER_FAVORITES);
                        intent.putExtra(ProductConstants.IS_FAVORITE, userFavorites.contains(productID));
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

    }
}