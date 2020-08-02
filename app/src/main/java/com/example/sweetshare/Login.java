package com.example.sweetshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweetshare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView emailField, passField;
    Button loginBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.EmailField);
        passField = findViewById(R.id.passwordField);
        loginBtn = findViewById(R.id.loginButton);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, pass;
                email = emailField.getText().toString().trim();
                pass = passField.getText().toString().trim();

                if (email.isEmpty()) {
                    emailField.setError("This field can't be empty.");
                }
                if (pass.isEmpty()) {
                    passField.setError("This field can't be empty.");
                }

                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in.", Toast.LENGTH_SHORT);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Displaying focus bar
        util.setInputFieldActivityStatus(findViewById(R.id.EmailField), findViewById(R.id.EmailFieldBar));
        util.setInputFieldActivityStatus(findViewById(R.id.passwordField), findViewById(R.id.PasswordFieldBar));
    }

    public void toSignupActivity(View view) {
        startActivity(new Intent(getApplicationContext(), Signup.class));
        finish();
    }

}