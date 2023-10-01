package com.dadm.tictactoeGame;

import java.util.Random;

public class TicTacToeGame {

    public enum DifficultyLevel{Easy, Hard, Expert}

    public static final int BOARD_SIZE = 9;
    private char[] mBoard = new char[BOARD_SIZE];

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';

    private final Random mRand;
    public DifficultyLevel mDifficultyLevel = null;
    public char playerControl;

    private int mHumanWins = 0;
    private int mAndroidWins = 0;
    private int mTies = 0;

    public TicTacToeGame() {
        mDifficultyLevel = DifficultyLevel.Hard;
        mRand = new Random();
    }

    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard(){
        for(int i = 0; i < BOARD_SIZE; i++){
            mBoard[i] = OPEN_SPOT;
        }
        playerControl = HUMAN_PLAYER;
    }

    /** Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public boolean setMove(char player, int location){
        boolean move = false;
        if(player == playerControl && location < BOARD_SIZE && mBoard[location] == OPEN_SPOT){
            mBoard[location] = player;
            if(playerControl == HUMAN_PLAYER){
                playerControl = COMPUTER_PLAYER;
            }else{
                playerControl = HUMAN_PLAYER;
            }
            move = true;
        }
        return move;
    }

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner(){
        int[][] winCombinations = new int[][] {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] combination : winCombinations) {
            if (mBoard[combination[0]] == HUMAN_PLAYER && mBoard[combination[1]] == HUMAN_PLAYER && mBoard[combination[2]] == HUMAN_PLAYER)
                return 2;
            if (mBoard[combination[0]] == COMPUTER_PLAYER && mBoard[combination[1]] == COMPUTER_PLAYER && mBoard[combination[2]] == COMPUTER_PLAYER)
                return 3;
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }
        return 1;
    }

    /**
     * Get a random move for the computer.
     * @return A random move for the computer (0-8).
     */
    private int getRandomMove(){
        int move;
        do{
            move = mRand.nextInt(BOARD_SIZE);
        }while(mBoard[move] != OPEN_SPOT);
        return move;
    }

    /**
     * Get a winning move for the computer if available.
     * @return The winning move if available, otherwise -1.
     */
    private int getWinningMove(){
        for(int i = 0; i < BOARD_SIZE; i++){
            if(mBoard[i] == OPEN_SPOT){
                char current = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if(checkForWinner() == 3){
                    mBoard[i] = OPEN_SPOT;
                    return i;
                }
                mBoard[i] = current;
            }
        }
        return -1;
    }

    /**
     * Get a blocking move to prevent the human player from winning.
     * @return The blocking move if available, otherwise -1.
     */
    private int getBlockingMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == OPEN_SPOT) {
                char current = mBoard[i];
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = OPEN_SPOT; // Restore the board state
                    return i;
                }
                mBoard[i] = current;
            }
        }
        return -1; // No blocking move found
    }

    public char getBoardOccupant(int i) {
        return mBoard[i];
    }

    public int getComputerMove(){
        int move = -1;
        if(mDifficultyLevel == DifficultyLevel.Easy){
            move = getRandomMove();
        } else if(mDifficultyLevel == DifficultyLevel.Hard){
            move = getWinningMove();
            if(move == -1){
                move = getRandomMove();
            }
        } else if (mDifficultyLevel == DifficultyLevel.Expert) {
            move = getWinningMove();
            if(move == -1){
                move = getBlockingMove();
                if(move == -1){
                    move = getRandomMove();
                }
            }
        }
        return move;
    }

    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        mDifficultyLevel = difficultyLevel;
    }

    public char[] getBoardState() {
        return mBoard;
    }

    public void setBoardState(char[] boardState){
        this.mBoard = boardState;
    }

    public char getPlayerTurn() {
        return playerControl;
    }

    public void setPlayerTurn(char turn){
        this.playerControl = turn;
    }

    public int getmHumanWins() {
        return mHumanWins;
    }

    public void setmHumanWins(int mHumanWins) {
        this.mHumanWins = mHumanWins;
    }

    public int getmAndroidWins() {
        return mAndroidWins;
    }

    public void setmAndroidWins(int mAnddroidWins) {
        this.mAndroidWins = mAnddroidWins;
    }

    public int getmTies() {
        return mTies;
    }

    public void setmTies(int mTies) {
        this.mTies = mTies;
    }
}
