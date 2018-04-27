package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FriendsAdapter friendsAdapter;
    private TextView userNameView;
    private TextView userEmailView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference();
    private Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();

        final RecyclerView recyclerView = findViewById(R.id.friends_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        navigationView.setNavigationItemSelectedListener(this);

        userNameView = navigationView.getHeaderView(0) .findViewById(R.id.userNameView);
        userEmailView = navigationView.getHeaderView(0).findViewById(R.id.userEmailView);

        final FriendsRepository friendsRepository = new FriendsRepository();

        friendsRepository.loadFriends(new FriendsRepository.FriendsLoadListener() {
            @Override
            public void onFriendsLoaded(List<Friend> friends) {
                friendsAdapter = new FriendsAdapter(friends);
                recyclerView.setAdapter(friendsAdapter);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent profileActivity = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileActivity);
            // Handle the camera action
        } else if (id == R.id.nav_exit) {
            FirebaseAuth.getInstance().signOut();
            onStart();
        } else if (id == R.id.nav_map) {
            Intent mapsActivity = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(mapsActivity);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        loadProfile();
        if (user != null) {
            userEmailView.setText(user.getEmail());
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        if (currentUser!=null) {

            // Toast.makeText(MainActivity.this, "You success!!", Toast.LENGTH_LONG).show();
            //  TextView textView = findViewById(R.id.textView);
//            textView.setText(currentUser.getEmail());
        }else{
            startActivity(intent);
        }
    }


    public void loadProfile(){
        profile = new Profile();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            mDatabase.child("profile").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    profile = dataSnapshot.getValue(Profile.class);
                    if(profile!=null) {
                        //   Toast.makeText(MainActivity.this, profile.getName(), Toast.LENGTH_SHORT).show();
                        userNameView.setText(profile.getName());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }}
}
