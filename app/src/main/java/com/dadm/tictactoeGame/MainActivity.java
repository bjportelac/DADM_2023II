package com.dadm.tictactoeGame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;

    private TicTacToeGame mGame;
    private TextView mInfoTextView;
    private TextView mScoreTextView;
    private int[] mScore;
    private BoardView mBoardView;
    private boolean mGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mScore = new int[3];
        mInfoTextView = (TextView) findViewById(R.id.information);
        mScoreTextView = (TextView) findViewById(R.id.score);
        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.play_grid);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        startNewGame();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty);

                final CharSequence[] levels = {
                        getResources().getString(R.string.dif_easy),
                        getResources().getString(R.string.dif_hard),
                        getResources().getString(R.string.dif_expert)};

                // selected is the radio button that should be selected.
                int selected = mGame.getDifficultyLevel().ordinal();

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                if(item == 0) {
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                } else if(item == 1) {
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Hard);
                                } else {
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                }

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;

            case DIALOG_ABOUT_ID:
                // Create the about confirmation dialog

                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();

                break;

            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;

        }

        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.Ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT_ID);
                return true;
        }
        return false;

    }

    private void startNewGame(){
        mGame.clearBoard();
        mBoardView.invalidate();
        mInfoTextView.setText(R.string.Human_turn);
    }

    private boolean setMove(char player, int location) {
        boolean move = false;
        if(mGame.setMove(player, location)){
            mBoardView.invalidate();
            move = true;
        }
        return move;
    }

    private void gameUpdater(int winner){
        if (winner == 0){
            mInfoTextView.setText(R.string.Human_new_turn);
        } else if (winner == 1){
            mInfoTextView.setText(R.string.Tie);
            mScore[0] = mScore[0] +1;
            mScoreTextView.setText("Player: "+mScore[1]+" Tie: "+mScore[0]+" Android: "+mScore[2]);
            mGameOver = true;
        } else if (winner == 2){
            mInfoTextView.setText(R.string.Human_wins);
            mScore[1] = mScore[1] +1;
            mScoreTextView.setText("Player: "+mScore[1]+" Tie: "+mScore[0]+" Android: "+mScore[2]);
            mGameOver = true;
        } else {
            mInfoTextView.setText(R.string.Computer_wins);
            mScore[2] = mScore[2] +1;
            mScoreTextView.setText("Player: "+mScore[1]+" Tie: "+mScore[0]+" Android: "+mScore[2]);
            mGameOver = true;
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int col = (int) motionEvent.getX() / mBoardView.getBoardCellWidth();
            int row = (int) motionEvent.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if(!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER,pos)){
                int winner = mGame.checkForWinner();
                if(winner == 0){
                    mInfoTextView.setText(R.string.Computer_turn);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int move = mGame.getComputerMove();
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            gameUpdater(mGame.checkForWinner());
                        }
                    },2000);
                }else{
                    gameUpdater(winner);
                }
            }
            return false;
        }
    };

}