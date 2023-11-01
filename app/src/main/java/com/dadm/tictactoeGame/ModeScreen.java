package com.dadm.tictactoeGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class ModeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_screen);
        FirebaseApp.initializeApp(this);

        final Button offline_button = findViewById(R.id.offline_but);
        final Button online_button = findViewById(R.id.online_but);

        offline_button.setOnClickListener(view -> {
            Intent intent = new Intent(ModeScreen.this, OfflineGameTTT.class);
            startActivity(intent);
            finish();
        });

        online_button.setOnClickListener( view -> {
            Intent intent = new Intent(ModeScreen.this, PlayerName.class);
            startActivity(intent);
            finish();
        });

    }
}