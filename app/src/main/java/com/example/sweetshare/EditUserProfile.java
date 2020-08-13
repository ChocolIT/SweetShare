package com.example.sweetshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class EditUserProfile extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private EditText nameInputField;
    private View nameInputFieldActivityBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharePrefEditor;
    private ImageView backButton;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        sharedPreferences = getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        sharePrefEditor = sharedPreferences.edit();

        nameInputFieldActivityBar = findViewById(R.id.nameInputFieldActivityBar);
        nameInputField = findViewById(R.id.nameInputField);
        backButton = findViewById(R.id.ic_back_arrow);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        documentReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid());

        nameInputField.setText(sharedPreferences.getString(UserConstants.USER_FULL_NAME, "Default"));
        ServicesHelper.setInputFieldActivityStatus(nameInputField, nameInputFieldActivityBar);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = nameInputField.getText().toString();

                if (updatedName.isEmpty()) {
                    nameInputField.setError("This field can't de empty");
                }
                else if (updatedName.length() < 3) {
                    nameInputField.setError("The name must contain at least 3 characters.");
                }
                else {
                    sharePrefEditor.putString(UserConstants.USER_FULL_NAME, updatedName);
                    sharePrefEditor.apply();
                    documentReference.update(UserConstants.USER_FULL_NAME, updatedName);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String updatedName = nameInputField.getText().toString();

        if (updatedName.isEmpty()) {
            nameInputField.setError("This field can't de empty");
        }
        else if (updatedName.length() < 3) {
            nameInputField.setError("The name must contain at least 3 characters.");
        }
        else {
            sharePrefEditor.putString(UserConstants.USER_FULL_NAME, updatedName);
            sharePrefEditor.apply();
            documentReference.update(UserConstants.USER_FULL_NAME, updatedName);
        }
        finish();
    }
}