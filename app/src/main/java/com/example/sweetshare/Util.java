package com.example.sweetshare;

import android.content.Context;
import android.content.Intent;
import android.util.EventLog;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Util {
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
                    Map<String, String> userData = new HashMap<>();

                    String userFullName = documentSnapshot.getString(Constants.USER_FULL_NAME);
                    userData.put(Constants.USER_FULL_NAME, userFullName);

                    String userEmail = documentSnapshot.getString(Constants.USER_EMAIL);
                    userData.put(Constants.USER_EMAIL, userEmail);


                    Intent intent = new Intent(contextOrigin, MainActivity.class);
                    intent.putExtra(Constants.USER_DATA, (Serializable) userData);

                    contextOrigin.startActivity(intent);

                }
            });
        }
}
