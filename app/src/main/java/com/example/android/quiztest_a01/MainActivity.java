package com.example.android.quiztest_a01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button highscoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highscoreButton = findViewById(R.id.highscore_button);
        // I'll just start the HighscoreActivity like this to test it out
        highscoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, HighscoreActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }
}
