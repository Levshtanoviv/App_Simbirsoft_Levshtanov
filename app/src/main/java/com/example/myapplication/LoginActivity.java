package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static aleksandrpolkin.ru.myproject.R.id.btnEntrance;

public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private final String TAG="myTag";
    private TextInputEditText email;
    private boolean lockemail=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        email = (TextInputEditText) findViewById(R.id.email);
        checkValid();
    }


    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this,"You success!!",Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }





    public void onClick(View v) {
        TextInputEditText email = (TextInputEditText) findViewById(R.id.email);
        TextInputEditText password = (TextInputEditText) findViewById(R.id.editPassword);

        checkEmail();

        // Check if user is signed in (non-null) and update UI accordingly.
        if(lockemail){
            switch (v.getId()) {
                case btnEntrance:
                    signIn(email.getText().toString().trim(),password.getText().toString().trim());
                    break;
                default:
                    break;
            }}
    }

    public void onClickRegistration(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
        finish();
    }


    public void checkValid() {
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkEmail();
            }

        });
    }

    public void checkEmail () {
        String s_email = email.getText().toString().trim();
        if (s_email.equals("") || !emailValid(s_email) || s_email.length() > 30) {
            email.setError("Email введен некорректно");
            lockemail = false;
        } else {
            lockemail = true;
        }
    }

    public final static boolean emailValid (String s_email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s_email).matches();
    }
}
