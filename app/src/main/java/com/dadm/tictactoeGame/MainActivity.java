package com.dadm.tictactoeGame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;

    private TicTacToeGame mGame;
    private TextView mInfoTextView;
    //Landscape mode
    private TextView mHumanScoreTextView;
    private TextView mComputerScoreTextView;
    private TextView mTiesScoreTextView;

    private BoardView mBoardView;
    private boolean mGameOver = false;
    private SharedPreferences mPrefs;

    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);

        mTiesScoreTextView = (TextView) findViewById(R.id.tie_score) ;
        mHumanScoreTextView = (TextView) findViewById(R.id.human_score);
        mComputerScoreTextView = (TextView) findViewById(R.id.android_score);
        mInfoTextView = (TextView) findViewById(R.id.information);

        mGame = new TicTacToeGame();

        mGame.setmHumanWins(mPrefs.getInt("humanWins", 0));
        mGame.setmAndroidWins(mPrefs.getInt("computerWins", 0));
        mGame.setmTies(mPrefs.getInt("ties", 0));

        int difficulty = mPrefs.getInt("difficultyLevel", TicTacToeGame.DifficultyLevel.Hard.ordinal());

        mBoardView = (BoardView) findViewById(R.id.play_grid);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);



        if(savedInstanceState == null){
            startNewGame();
        }else{
            mGame.setBoardState(savedInstanceState.getCharArray("mBoard"));
            mGameOver = savedInstanceState.getBoolean("mGameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("mInfo"));
            mGame.setPlayerTurn(savedInstanceState.getChar("mPlayerTurns"));
        }
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
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;

            case DIALOG_QUIT_ID:
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
            case R.id.reset_scores:
                resetGame();
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT_ID);
                return true;
        }
        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.burst);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.pop);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("humanWins", this.mGame.getmHumanWins());
        ed.putInt("computerWins", this.mGame.getmAndroidWins());
        ed.putInt("ties", this.mGame.getmTies());
        ed.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharArray("mBoard",mGame.getBoardState());
        outState.putBoolean("mGameOver",mGameOver);
        outState.putCharSequence("mInfo",mInfoTextView.getText());
        outState.putInt("mHumanWins",mGame.getmHumanWins());
        outState.putInt("mTies",mGame.getmTies());
        outState.putInt("mAndroidWins",mGame.getmAndroidWins());
        outState.putChar("mPlayerTurns", mGame.getPlayerTurn());

    }

    private void displayScores() {
        mHumanScoreTextView.setText(Integer.toString(this.mGame.getmHumanWins()));
        mComputerScoreTextView.setText(Integer.toString(this.mGame.getmAndroidWins()));
        mTiesScoreTextView.setText(Integer.toString(this.mGame.getmTies()));
    }

    private boolean setMove(char player, int location) {
        boolean move = false;
        if(mGame.setMove(player, location)){
            mBoardView.invalidate();
            if(player == TicTacToeGame.HUMAN_PLAYER){
                mHumanMediaPlayer.start();
            }else{
                mComputerMediaPlayer.start();
            }
            move = true;
        }
        return move;
    }

    private void gameUpdater(int winner){
        if (winner == 0){
            mInfoTextView.setText(R.string.Human_new_turn);
        } else if (winner == 1){
            mInfoTextView.setText(R.string.Tie);
            mGame.setmTies(mGame.getmTies()+1);
            mTiesScoreTextView.setText("Ties: "+mGame.getmTies());
            mGameOver = true;
        } else if (winner == 2){
            mInfoTextView.setText(R.string.Human_wins);
            mGame.setmHumanWins(mGame.getmHumanWins()+1);
            mHumanScoreTextView.setText("Player: "+mGame.getmHumanWins());
            mGameOver = true;
        } else {
            mInfoTextView.setText(R.string.Computer_wins);
            mGame.setmAndroidWins(mGame.getmAndroidWins()+1);
            mComputerScoreTextView.setText("Android: "+mGame.getmAndroidWins());
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

    private void resetGame() {
        this.mGame.setmTies(0);
        this.mGame.setmAndroidWins(0);
        this.mGame.setmHumanWins(0);
        displayScores();
        startNewGame();
    }

    private void startNewGame(){
        mGame.clearBoard();
        mGameOver = false;

        mGame.clearBoard();
        mBoardView.invalidate();

        mInfoTextView.setText(R.string.Human_turn);
    }
}