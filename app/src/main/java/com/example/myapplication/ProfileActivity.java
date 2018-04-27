package com.example.myapplication;

import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Profile profile;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference();
    private String TAG = "MyTag";
    private String id;
    private TextInputEditText email;
    private TextInputEditText phone ;
    private TextInputEditText name ;
    private RoundedImageView avatar;
    private boolean lockname = true;
    private boolean lockemail = true;
    private boolean lockphone = true;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = (TextInputEditText) findViewById(R.id.email);
        phone = (TextInputEditText) findViewById(R.id.phone);
        name = (TextInputEditText) findViewById(R.id.name);
        avatar = (RoundedImageView) findViewById(R.id.avatar);
        save = (Button) findViewById(R.id.save);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            id = user.getUid();
        }
        final Handler handler = new Handler();
        new Thread() {
            public void run() {
                loadProfile();
                handler.post(new Runnable() {
                    public void run() {
                        checkValid();
                    }
                });
            }
        }.start();


    }


    public void onClick(View v) {

        checkEmail();
        checkName();
        checkPhone();
        if(lockname && lockemail && lockphone){
            profile = new Profile();
            profile.setProfile(name.getText().toString(),phone.getText().toString(),email.getText().toString(),null);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(ProfileActivity.this,"Success",Toast.LENGTH_SHORT).show();
               /* if(user != null) {
                    user.updateEmail(email.getText().toString().trim());
                }*/
            mDatabase.child("profile").child(id).setValue(profile);
            finish();
        } else {
            Toast.makeText(ProfileActivity.this, "Форма введена не верно", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadProfile(){
        mDatabase.child("profile").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(Profile.class);
                if(profile != null){
                    outDispley(profile);
                }
                else{
                    outEmail();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void outEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email.setText(user.getEmail());
    }

    public void outDispley(Profile profile){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (profile.getEmail().equals("")) {

            if (user != null) {
                email.setText(user.getEmail());
                // id = user.getUid();
            }
        } else {
            email.setText(profile.getEmail());
        }
        phone.setText(profile.getPhone());
        name.setText(profile.getName());

    }



    public void checkValid(){
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkEmail();
            }

        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkName();
            }});

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkPhone();
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

    public void checkName(){
        String s_name = name.getText().toString().trim();
        if (s_name.equals("") || s_name.length()>30) {
            name.setError("Имя введено некорректно");
            lockname = false;
        } else {lockname = true;}
    }

    public void checkPhone(){
        String s_phone = phone.getText().toString().trim();
        if (s_phone.equals("") || !phoneValid(s_phone) || s_phone.length()>12) {

            phone.setError("Телефон введен некорректно");
            lockphone = false;
        }else{
            lockphone = true;}
    }

    public final static boolean emailValid(String s_email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s_email).matches();
    }

    private final static boolean phoneValid(String s_phone) {
        return Patterns.PHONE.matcher(s_phone).matches();
    }

       /* private final static boolean nameValid(String s_name) {
        return true;
        }*/

    //end
}


