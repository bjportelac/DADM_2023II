package com.dadm.tictactoeGame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PlayerName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_name);

        final EditText playerNameET = findViewById(R.id.nickname_field);
        final AppCompatButton startOnlineACB = findViewById(R.id.start_online);

        startOnlineACB.setOnClickListener(view -> {
            final String playerName = playerNameET.getText().toString();
            if(playerName.isEmpty()){
                Toast.makeText(PlayerName.this, "Can't begin without a nickname", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(PlayerName.this, OnlineGameTTT.class);
                intent.putExtra("playerNickName",playerName);
                startActivity(intent);
                finish();
            }
        });
    }
}