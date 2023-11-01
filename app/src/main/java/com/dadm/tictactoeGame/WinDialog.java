package com.dadm.tictactoeGame;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class WinDialog extends Dialog {

    private final String message;
    private final OnlineGameTTT onlineGameTTT;

    public WinDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
        this.onlineGameTTT = ((OnlineGameTTT) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_dialog_layout);

        final TextView messageTextView = findViewById(R.id.message_textview);
        final Button startAgainBtn = findViewById(R.id.startNewButton);
        final Button modeSelectionBtn = findViewById(R.id.modeSelectionScreen);

        messageTextView.setText(message);
        startAgainBtn.setOnClickListener(v -> {
            dismiss();
            getContext().startActivity(new Intent(getContext(), PlayerName.class));
            onlineGameTTT.finish();
        });

        modeSelectionBtn.setOnClickListener(v -> {
            dismiss();
            getContext().startActivity(new Intent(getContext(), ModeScreen.class));
            onlineGameTTT.finish();
        });
    }
}
