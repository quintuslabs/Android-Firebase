package com.quintus.labs.androidfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.quintus.labs.androidfirebase.R;

public class ForgotPassword extends AppCompatActivity {
   EditText email;
   String _email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.email);
    }

    public void onResetClicked(View view) {
        _email = email.getText().toString().trim();
        if(_email.length()==0){
            email.setError("Please Enter Email");
            email.requestFocus();
        }else{
             auth.sendPasswordResetEmail(_email) .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ForgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}