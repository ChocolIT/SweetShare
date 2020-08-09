package com.example.sweetshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditUserProfile extends AppCompatActivity {

    private ConstraintLayout profileImgPreviewLayout;
    private ImageView profileImgPreview;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        profileImgPreviewLayout = findViewById(R.id.changeProfileImgLayout);
        profileImgPreview = findViewById(R.id.profileImgPreview);

        profileImgPreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, RequestCodes.GALLERY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCodes.GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri profileImgUri = data.getData();
                profileImgPreview.setImageURI(profileImgUri);
                uploadImageToFirebase(profileImgUri);
            }
        }
    }

    private void uploadImageToFirebase (Uri imageUri) {
        StorageReference fileRef = storageReference.child("profile-images/" + fAuth.getCurrentUser().getUid() + ".jpg");
        fileRef.putFile(imageUri);
    }
}