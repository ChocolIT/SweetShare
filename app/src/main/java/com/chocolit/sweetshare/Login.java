package com.chocolit.sweetshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Login extends AppCompatActivity {

    private TextView emailField, passField;
    private Button loginBtn;
    private FirebaseAuth fAuth;
    private ConstraintLayout loadingLayout;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSignInButton;

    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.EmailField);
        passField = findViewById(R.id.passwordField);
        loginBtn = findViewById(R.id.loginButton);
        loadingLayout = findViewById(R.id.loadingLayout);

        googleSignInButton = findViewById(R.id.googleSignInButton);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, pass;

                email = emailField.getText().toString().trim();
                pass = passField.getText().toString().trim();

                if (email.isEmpty()) {
                    emailField.setError("This field can't be empty.");
                    return;
                }
                if (pass.isEmpty()) {
                    passField.setError("This field can't be empty.");
                    return;
                }

                loadingLayout.setVisibility(ConstraintLayout.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ServicesHelper.fetchUserDataFromFireStoreAndStartMainActivity(fAuth.getCurrentUser().getUid(), Login.this);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(Login.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            loadingLayout.setVisibility(ConstraintLayout.INVISIBLE);
                        }
                    }
                });
            }
        });

        //Displaying focus bar
        ServicesHelper.setInputFieldActivityStatus(findViewById(R.id.EmailField), findViewById(R.id.EmailFieldBar));
        ServicesHelper.setInputFieldActivityStatus(findViewById(R.id.passwordField), findViewById(R.id.PasswordFieldBar));
    }

    public void toSignupActivity(View view) {
        startActivity(new Intent(getApplicationContext(), Signup.class));
        finish();
    }

    public void resetPassword(View view) {
        startActivity(new Intent(getApplicationContext(), PasswordRecovery.class));
        finish();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT);
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            Toast.makeText(Login.this, "Error: " + e, Toast.LENGTH_SHORT);
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        fAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT);
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    String uID = fAuth.getCurrentUser().getUid();
                    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = fStore.collection("users").document(uID);

                    Map<String, Object> userData = new HashMap<>();
                    userData.put(UserConstants.USER_FULL_NAME, acct.getDisplayName());
                    userData.put(UserConstants.USER_EMAIL, acct.getEmail());
                    userData.put(UserConstants.USER_REPUTATION, 0);
                    ArrayList<String> ownedProducts = new ArrayList<>();
                    userData.put(UserConstants.USER_OWNED_PRODUCTS_LIST, ownedProducts);

                    ArrayList<String> favoriteProducts = new ArrayList<>();
                    userData.put(UserConstants.USER_FAVORITES, favoriteProducts);

                    documentReference.set(userData);
                    ServicesHelper.fetchUserDataFromFireStoreAndStartMainActivity(uID, Login.this);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(Login.this, "Error: " + task.getException(), Toast.LENGTH_SHORT);
                }
            }
        });
    }
}