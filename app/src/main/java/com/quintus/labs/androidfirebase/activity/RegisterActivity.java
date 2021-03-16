package com.quintus.labs.androidfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quintus.labs.androidfirebase.R;
import com.quintus.labs.androidfirebase.model.User;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity==>";
    EditText name, email, password;
    String _name, _email, _password;
    private FirebaseAuth auth;
    User user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

    }

    public void onRegisterClicked(View view) {
        _name = name.getText().toString().trim();
        _email = email.getText().toString().trim();
        _password = password.getText().toString().trim();


        if (_name.length() == 0) {
            name.setError("Please Enter Name");
            name.requestFocus();
        } else if (_email.length() == 0) {
            email.setError("Please Enter Email");
            email.requestFocus();
        } else if (_password.length() < 6) {
            password.setError("Please Enter 6 alpha numeric password");
            password.requestFocus();
        } else {
            user = new User(_name, _email, "", "", "", "");
            registerUser();
        }

    }

    private void registerUser() {
        auth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();
                            createUserProfile(uid);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void createUserProfile(String uid) {
        db.collection("users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

}