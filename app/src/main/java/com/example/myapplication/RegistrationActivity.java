package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {


    private Profile profile;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference();
    private String TAG = "MyTag";
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText password_repeat;
    private boolean lockemail = true;
    private boolean lockpassword = true;
    private boolean lockpassword_repeat = true;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = (TextInputEditText) findViewById(R.id.email);
        save = (Button) findViewById(R.id.save);
        password = (TextInputEditText) findViewById(R.id.password);
        password_repeat = (TextInputEditText) findViewById(R.id.password_repeat);

        checkValid();
    }






   /* public void outDispley(Profile profile){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(profile != null) {
            if (profile.getEmail() == null) {

                if (user != null) {
                    email.setText(user.getEmail());
                    id = user.getUid();
                }
            } else {
                email.setText(profile.getEmail());
            }
            phone.setText(profile.getPhone());
            name.setText(profile.getName());
        }else{
            Toast.makeText(RegistrationActivity.this,"error",Toast.LENGTH_SHORT).show();}
    }*/


    public void checkValid(){
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkEmail();
            }

        });

        password_repeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkPassword_repeat();
            }

        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkPassword();
            }

        });

    }

    public void checkEmail(){
        String s_email = email.getText().toString().trim();
        if (s_email.equals("") || !emailValid(s_email) || s_email.length()>30) {
            email.setError("Email введен некорректно");
            lockemail = false;
        }else{
            lockemail = true;}
    }

    public final static boolean emailValid(String s_email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s_email).matches();
    }


    public void checkPassword(){
        String s_password = password.getText().toString().trim();
        if (s_password.equals("") || s_password.length()>30 || !isPasswordValidMethod(s_password) || s_password.length()<6) {
            password.setError("Пароль введен некорректно");
            lockpassword = false;
        } else {lockpassword = true;}
    }

    public static boolean isPasswordValidMethod(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[A-Za-z]).(?=.*[A-Za-z0-9]).*$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void checkPassword_repeat(){
        String s_password = password.getText().toString().trim();
        String s_password_repeat = password_repeat.getText().toString().trim();
        if (s_password.equals(s_password_repeat)) {
            lockpassword_repeat = true;
        } else {
            password_repeat.setError("Не совпадает с паролем");
            lockpassword_repeat = false;}
    }



    public void onClick(View view) {

        checkEmail();
        checkPassword_repeat();
        checkPassword();

        if(lockemail && lockpassword && lockpassword_repeat){
            createUser(email.getText().toString().trim(),password.getText().toString().trim());
            // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(RegistrationActivity.this,"Success",Toast.LENGTH_SHORT).show();
               /* if(user != null) {
                    user.updateEmail(email.getText().toString().trim());
                }*/

            finish();
        } else {
            Toast.makeText(RegistrationActivity.this, "Форма введена не верно", Toast.LENGTH_SHORT).show();
        }
    }

    public void createUser(String email, String password){

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //end
}
