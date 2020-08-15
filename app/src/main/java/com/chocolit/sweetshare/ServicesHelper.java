package com.chocolit.sweetshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class ServicesHelper {
        public static void setInputFieldActivityStatus(View inputField, final View activityBar) {
            /** Adds onFocusChangeListener to a View and displays/hides its activity bar based on it.
             * param: [View] The view we want to add the listener to.
             * param: [View] The activity bar corresponding to view.
             */
            inputField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (view.hasFocus()) {
                        activityBar.setVisibility(View.VISIBLE);
                    }
                    else {
                        activityBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        public static void fetchUserDataFromFireStoreAndStartMainActivity(String uID, final Context contextOrigin) {
            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = fStore.collection("users").document(uID);

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    SharedPreferences sharedPreferences = contextOrigin.getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String userFullName = documentSnapshot.getString(UserConstants.USER_FULL_NAME);
                    editor.putString(UserConstants.USER_FULL_NAME, userFullName);

                    String userEmail = documentSnapshot.getString(UserConstants.USER_EMAIL);
                    editor.putString(UserConstants.USER_EMAIL, userEmail);

                    Long userReputation = (Long) documentSnapshot.get(UserConstants.USER_REPUTATION);
                    editor.putLong(UserConstants.USER_REPUTATION, userReputation);

                    editor.apply();

                    Intent intent = new Intent(contextOrigin, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    contextOrigin.startActivity(intent);
                    ((Activity)contextOrigin).finish();

                }
            });
        }

        public static int getStarIconFill (Long userRep, int multiplier) {

            if (userRep <= 20 * (multiplier - 1)) {
                return UserConstants.EMPTY_REVIEW_STAR;
            }
            else if (userRep > 20 * (multiplier - 1) && userRep <= 20 * (multiplier) - 10) {
                return UserConstants.HALF_REVIEW_STAR;
            }
            else {
                return UserConstants.FULL_REVIEW_STAR;
            }
        }
}
