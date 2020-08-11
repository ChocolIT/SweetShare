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

    private ConstraintLayout profileImgPreviewLayout;
    private ImageView profileImgPreview;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;
    private EditText nameInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        profileImgPreviewLayout = findViewById(R.id.changeProfileImgLayout);
        profileImgPreview = findViewById(R.id.profileImgPreview);

        SharedPreferences sharedPreferences = getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(UserConstants.USER_HAS_CUSTOM_PICTURE, false)) {
            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Picasso
                    .get()
                    .load(sharedPreferences.getString(UserConstants.USER_PROFILE_PICTURE_URI, "Default"))
                    .fit().centerCrop().into(profileImgPreview);
        }

        profileImgPreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, RequestCodes.GALLERY_REQUEST_CODE);
            }
        });

        ServicesHelper.setInputFieldActivityStatus(findViewById(R.id.nameInputField), findViewById(R.id.nameInputFieldActivityBar));

        nameInputField = findViewById(R.id.nameInputField);
        nameInputField.setText(sharedPreferences.getString(UserConstants.USER_FULL_NAME, "Default"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCodes.GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri profileImgUri = data.getData();
                profileImgPreview.setImageURI(profileImgUri);
                uploadImageToFirebase(profileImgUri);

                String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uId);
                documentReference.update(UserConstants.USER_HAS_CUSTOM_PICTURE, true);

                SharedPreferences sharedPreferences = getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(UserConstants.USER_HAS_CUSTOM_PICTURE, true);
            }
        }
    }

    private void uploadImageToFirebase (Uri imageUri) {
        StorageReference fileRef = storageReference.child("profile-images/" + fAuth.getCurrentUser().getUid() + ".jpg");
        fileRef.putFile(imageUri);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPreferences = getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!nameInputField.getText().toString().trim().equals(sharedPreferences.getString(UserConstants.USER_FULL_NAME, "Default"))) {
            editor.putString(UserConstants.USER_FULL_NAME, nameInputField.getText().toString().trim());
            editor.commit();
        }
        finish();
    }

    public void backButton(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(UserConstants.USER_FETCHED_DATA_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!nameInputField.getText().toString().trim().equals(sharedPreferences.getString(UserConstants.USER_FULL_NAME, "Default"))) {
            editor.putString(UserConstants.USER_FULL_NAME, nameInputField.getText().toString().trim());
            editor.commit();
        }
        finish();
    }
}