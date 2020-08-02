package com.example.sweetshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.sweetshare.R;

public class Signup extends AppCompatActivity {

    EditText fullNameField, emailField, passField, repPassField;
    Button signupButton;
    TextView toLoginButton;
    FirebaseAuth fAuth;
    Layout loadingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullNameField = findViewById(R.id.NameField);
        emailField = findViewById(R.id.EmailField);
        passField = findViewById(R.id.PasswordField);
        repPassField = findViewById(R.id.RepPasswordField);
        signupButton = findViewById(R.id.SignupButton);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = fullNameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String pass = passField.getText().toString();
                String passConfirmation = repPassField.getText().toString();

                if (fullName.isEmpty()) {
                    fullNameField.setError("This field can't be empty");
                }
                if (email.isEmpty()) {
                    emailField.setError("This field can't be empty");
                }
                if (pass.isEmpty()){
                    passField.setError("This field can't be empty");
                }
                if (passConfirmation.isEmpty()) {
                    repPassField.setError("This field can't be empty");
                }

                if (!pass.equals(passConfirmation)) {
                    passField.setError("The passwords don't match");
                }
                if (pass.length() < 6) {
                    passField.setError("Password must be at least 6 characters long");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Signup.this, "Account created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(Signup.this, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void toLoginActivity(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}