package com.quintus.labs.androidfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quintus.labs.androidfirebase.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private static final String TAG = "LoginActivity==>";
    EditText email,password;
    String _email,_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();
    }

    public void onLoginClicked(View view) {
        _email = email.getText().toString().trim();
        _password = password.getText().toString().trim();

         if (_email.length()==0){
            email.setError("Please Enter Email");
            email.requestFocus();
        }
        else if (_password.length()<6){
            password.setError("Please Enter 6 alpha numeric password");
            password.requestFocus();
        }else{
            loginUser();
        }
    }

    private void loginUser() {
        auth.signInWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void onRegisterClicked(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }


    public void onForgotPasswordClicked(View view) {
        startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
    }
}