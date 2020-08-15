package com.chocolit.sweetshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecovery extends AppCompatActivity {

    private EditText emailField;
    private FirebaseAuth fAuth;
    private Button sendLinkBtn;
    private TextView backToLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        emailField = findViewById(R.id.emailField);
        sendLinkBtn = findViewById(R.id.button);
        backToLoginActivity = findViewById(R.id.toLogin);

        fAuth = FirebaseAuth.getInstance();

        sendLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();

                if (email.isEmpty()){
                    emailField.setError("This field can't be empty.");
                    return;
                }
                findViewById(R.id.loadingOverlay).setVisibility(ConstraintLayout.VISIBLE);

                findViewById(R.id.successMessage).setVisibility(TextView.INVISIBLE);
                findViewById(R.id.failureMessage).setVisibility(TextView.INVISIBLE);

                fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        findViewById(R.id.successMessage).setVisibility(TextView.VISIBLE);
                        findViewById(R.id.loadingOverlay).setVisibility(ConstraintLayout.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        findViewById(R.id.failureMessage).setVisibility(TextView.VISIBLE);
                        findViewById(R.id.loadingOverlay).setVisibility(ConstraintLayout.INVISIBLE);
                    }
                });
            }
        });

        ServicesHelper.setInputFieldActivityStatus(emailField, findViewById(R.id.focusActivityBar));
    }

    public void toLoginActivity(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}