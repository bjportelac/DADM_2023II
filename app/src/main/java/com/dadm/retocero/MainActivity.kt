package com.dadm.retocero

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dadm.retocero.dto.TicTacToeGame
import com.dadm.retocero.uiElements.DifficultyDialogFragment
import com.dadm.retocero.uiElements.QuitDialogFragment

class MainActivity : AppCompatActivity() {

    private var mGame: TicTacToeGame? = null //Represents the internal state of the game
    private lateinit var mBoardButtons: Array<Button> // Buttons making up the board
    private lateinit var mInfoTextView: TextView // Various text displayed
    private lateinit var mReset: Button
    private var mGameDifficulty: Int = 0
    private var mPlayerScore: Int = 0
    private var mComputerScore: Int = 0

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

        updateScoresAndDisplay()

        print(mGameDifficulty)
        mGame = TicTacToeGame(mGameDifficulty)
        println(mGame?.mDifficultyLevel)

        startNewGame()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.new_game ->{
                startNewGame()
                true
            }
            R.id.ai_difficulty ->{
                val selected = mGameDifficulty
                val dialog = DifficultyDialogFragment(selected){ item ->
                    setGameDifficulty(item)
                }
                dialog.show(supportFragmentManager,"DifficultyDialog")
                true
            }
            R.id.quit -> {
                val dialog = QuitDialogFragment()
                dialog.show(supportFragmentManager, "QuitDialog")
                true
            }
            else -> {
                false
            }
        }
    }

    private fun setGameDifficulty(difficulty: Int){
        mGameDifficulty = if(difficulty >= 3){2;}else{difficulty}
        mGame = TicTacToeGame(difficulty)
        clearVisibleBoard()
        mGame?.clearBoard()
    }

    private fun startNewGame(){
        mGame = TicTacToeGame(mGameDifficulty)
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
                2 -> {
                    mInfoTextView.text = resources.getText(R.string.result_human_wins)
                    mPlayerScore++
                    updateScoresAndDisplay()

                }
                else -> {
                    mInfoTextView.text = resources.getText(R.string.result_computer_wins)
                    mComputerScore++
                    updateScoresAndDisplay()
                }
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

    @SuppressLint("SetTextI18n")
    private fun updateScoresAndDisplay() {
        val playerScoreTextView = findViewById<TextView>(R.id.player_score)
        val computerScoreTextView = findViewById<TextView>(R.id.computer_score)

        playerScoreTextView.text = "Player: $mPlayerScore"
        computerScoreTextView.text = "Computer: $mComputerScore"
    }

}
