package com.example.android.quiztest_a01;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

    // Views
    Button highScoreButton;
    EditText userNameEditText;
    EditText highscoreValueEditText;
    TextView highscoreTextView;
    TextView nameTextView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mHighscoreDatabaseReference;
    private ChildEventListener mChildEventListener;

    // for display all the highscore views
    private ListView mHighscoreListView;
    private HighscoreAdapter mHighscoreAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_layout);

        /* Getting the database and the reference child we want to refer
         * in this case highscore
         */
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mHighscoreDatabaseReference = mFirebaseDatabase.getReference().child("highscore");


        // Button to send userNameEditText & highscoreValueEditText values to the database
        highScoreButton = findViewById(R.id.send_highscore_button);
        userNameEditText = findViewById(R.id.user_name_edit_text);
        highscoreValueEditText = findViewById(R.id.highscore_edit_text);

        // Views for display back data from the FireBase (TEST)
        highscoreTextView = findViewById(R.id.highscore_text_view);
        nameTextView = findViewById(R.id.name_text_view);

        // display highscore views
        mHighscoreListView = findViewById(R.id.highscore_listview);
        // Initialize message ListView and its adapter
        List<HighscoreMessage> highscoreMessages = new ArrayList<>();
        mHighscoreAdapter = new HighscoreAdapter(this, R.layout.highscore_message_layout, highscoreMessages);
        mHighscoreListView.setAdapter(mHighscoreAdapter);

        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting userNameEditText & highscoreValueEditText from views
                String nameString = MainActivity.mUsername;
                String highscoreString = highscoreValueEditText.getText().toString();

                // create a new HighscoreMessage with those String values
                HighscoreMessage highscoreMessage = new HighscoreMessage(nameString, highscoreString);
                // push it to the database
                mHighscoreDatabaseReference.push().setValue(highscoreMessage);

                /**
                 * IMPORTANT NOTE:
                 * right now I changed the database .read and .write rules and you need to be athenticated
                 * to .read and .write
                 */

                // then, clear all the EditText views
                userNameEditText.clearComposingText();
                highscoreValueEditText.clearComposingText();
            }
        });

        /** This is a listener to retrieve data from FireBase
         *  every time a new child is added (a new highscore is sended) it update the
         *  text views for display the highscore and username
         */
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                HighscoreMessage highScoreMessage = dataSnapshot.getValue(HighscoreMessage.class);

                nameTextView.setText(highScoreMessage.getUsername());
                highscoreTextView.setText(highScoreMessage.getHighscore());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        mHighscoreDatabaseReference.addChildEventListener(mChildEventListener);

    }
}