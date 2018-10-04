package com.example.android.quiztest_a01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Button highscoreButton;

    public static final String ANONYMOUS = "anonymous";
    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    public static String mUsername;
    /* Those are needed to authenticate the user
     * I dont' know if it's better to do it like this or open a pop up window
     * or something else.. I just leave here the code
     */
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mHighscoreDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        // getting the database
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // getting the Authentication
        mFirebaseAuth = FirebaseAuth.getInstance();

        highscoreButton = findViewById(R.id.highscore_button);

        // I'll just start the HighscoreActivity like this to test it out
        highscoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, HighscoreActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        // this will check if the user's already logged in or not
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    Toast.makeText(MainActivity.this, "You're now signed in", Toast.LENGTH_SHORT).show();
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    /* user is not signed in
                     * this will add all the method we will need for authentication
                     * right now I choose only email and Google, but I leave here
                     * also Facebook, just in case
                     */
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            /*new AuthUI.IdpConfig.FacebookBuilder().build(),*/
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_out_item:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method to check if the login was succesfull or failed
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // if the app goes in the background this will remove the listener
    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        mUsername = ANONYMOUS;
    }

    // if the app will open again this will add the listener
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onSignedInInitialize(String username) {
        mUsername = username;
    }

    public void onSignedOutCleanUp() {
        mUsername = ANONYMOUS;
    }
}
