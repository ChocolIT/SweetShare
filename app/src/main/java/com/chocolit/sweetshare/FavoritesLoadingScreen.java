package com.chocolit.sweetshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoritesLoadingScreen extends AppCompatActivity {
    int completed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ArrayList<String> productNames = new ArrayList<>();
        final ArrayList<String> productCities = new ArrayList<>();
        final ArrayList<String> productPrices = new ArrayList<>();
        final ArrayList<String> productImgs = new ArrayList<>();
        final ArrayList<String> productIds = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_loading_screen);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference userDoc = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final ArrayList<String> userFavoritesList = (ArrayList<String>) documentSnapshot.get(UserConstants.USER_FAVORITES);
                for(String prodID : userFavoritesList){
                    DocumentReference productDoc = firebaseFirestore.collection("products").document(prodID);
                    productDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> imgList = (ArrayList<String>) documentSnapshot.get(ProductConstants.PRODUCT_IMG_LIST);
                            productNames.add(documentSnapshot.getString(ProductConstants.PRODUCT_TITLE));
                            productPrices.add(String.valueOf(documentSnapshot.getLong(ProductConstants.PRICE)));
                            productCities.add(documentSnapshot.getString(ProductConstants.PRODUCT_CITY));
                            productImgs.add(imgList.get(0));
                            productIds.add(documentSnapshot.getString(ProductConstants.ID));

                            completed++;
                            if (completed == userFavoritesList.size()) {
                                Log.d("TAG", "onSuccess: " + productNames.size());

                                Intent intent = new Intent(getApplicationContext(), Favorites.class);
                                intent.putStringArrayListExtra(ProductConstants.PRODUCT_TITLE, productNames);
                                intent.putStringArrayListExtra(ProductConstants.PRICE, productPrices);
                                intent.putStringArrayListExtra(ProductConstants.PRODUCT_CITY, productCities);
                                intent.putStringArrayListExtra(ProductConstants.PRODUCT_IMG_LIST, productImgs);
                                intent.putStringArrayListExtra(ProductConstants.ID, productIds);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}