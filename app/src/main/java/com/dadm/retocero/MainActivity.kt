package com.dadm.retocero

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.dadm.retocero.dto.TicTacToeGame

class MainActivity : ComponentActivity() {

    private var mGame: TicTacToeGame? = null //Represents the internal state of the game
    private lateinit var mBoardButtons: Array<Button> // Buttons making up the board
    private lateinit var mInfoTextView: TextView // Various text displayed
    private lateinit var mReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        mBoardButtons = Array(TicTacToeGame.BOARD_SIZE){ Button(this)}
        mBoardButtons[0] = findViewById(R.id.one)
        mBoardButtons[1] = findViewById(R.id.two)
        mBoardButtons[2] = findViewById(R.id.three)
        mBoardButtons[3] = findViewById(R.id.four)
        mBoardButtons[4] = findViewById(R.id.five)
        mBoardButtons[5] = findViewById(R.id.six)
        mBoardButtons[6] = findViewById(R.id.seven)
        mBoardButtons[7] = findViewById(R.id.eight)
        mBoardButtons[8] = findViewById(R.id.nine)
        mInfoTextView = findViewById(R.id.information)

        mReset = findViewById(R.id.newGame)
        mReset.setOnClickListener {
            startNewGame()
        }

        mGame = TicTacToeGame()

        startNewGame()


    }

    private fun startNewGame(){
        clearVisibleBoard()
        mGame?.clearBoard()
        mInfoTextView.text = resources.getText(R.string.first_human)
    }

    private fun clearVisibleBoard(){
        mBoardButtons.forEachIndexed { index, button ->
            button.text = " "
            button.isEnabled = true
            button.setOnClickListener{ onClickListener(index) }
        }
    }

    private fun onClickListener(index: Int) {
        if(mBoardButtons[index].isEnabled){
            setMove(TicTacToeGame.HUMAN_PLAYER,index)

            var winner = mGame?.checkForWinner()
            if(winner == 0){
                mInfoTextView.text = resources.getText(R.string.turn_computer)
                val move : Int? = mGame?.computerMove
                if (move != null) {
                    setMove(TicTacToeGame.COMPUTER_PLAYER,move)
                }
                winner = mGame?.checkForWinner()
            }

            when(winner){
                0 -> mInfoTextView.text = resources.getText(R.string.turn_human)
                1 -> mInfoTextView.text = resources.getText(R.string.result_tie)
                2 -> mInfoTextView.text = resources.getText(R.string.result_human_wins)
                else -> mInfoTextView.text = resources.getText(R.string.result_computer_wins)
            }
        }
    }

    private fun setMove(player:Char, location: Int){
        mGame?.setMove(player, location)
        mBoardButtons[location].isEnabled = false
        mBoardButtons[location].text = player.toString()
        if(player == TicTacToeGame.HUMAN_PLAYER){
            mBoardButtons[location].setTextColor(Color.rgb(0,200,0))
        }else{
            mBoardButtons[location].setTextColor(Color.rgb(200,0,0))
        }
    }

}
