package com.dadm.retocero.dto;

import java.util.Random;

public class TicTacToeGame {



    //Computer difficulty level
    public enum DifficultyLevel{Easy, Hard, Expert};

    public static final int BOARD_SIZE = 9;
    private char mBoard[] = new char[BOARD_SIZE];

    // Characters used to represent the human, computer, and open spots
    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';

    public DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;
    private Random mRand;

    public TicTacToeGame() {
        mRand = new Random();
    }

    public TicTacToeGame (int difficultyLevel){
        mRand = new Random();
        if(difficultyLevel == 0){
            this.mDifficultyLevel = DifficultyLevel.Easy;
        } else if(difficultyLevel == 1){
            this.mDifficultyLevel = DifficultyLevel.Hard;
        } else if (difficultyLevel == 2) {
            this.mDifficultyLevel = DifficultyLevel.Expert;
        }
    }

    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard(){
        for(int i = 0; i < BOARD_SIZE; i++){
            mBoard[i] = OPEN_SPOT;
        }
    }

    /** Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    public void setMove(char player, int location){
        if(location < BOARD_SIZE && mBoard[location] == OPEN_SPOT){
            mBoard[location] = player;
        }
    }

    /**
     * Get a random move for the computer.
     * @return A random move for the computer (0-8).
     */
    public int getRandomMove(){
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
    public int getWinningMove(){
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
    public int getBlockingMove() {
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

    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
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

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner(){

        int[][] winCombinations = new int[][] {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
                {0, 4, 8}, {2, 4, 6}  // diagonals
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
     * Returns the char representing the token of the player on a board position
     * @param i which corresponds to the index to consult
     * @return char
     */
    public char getBoardOccupant(int i) {
        return mBoard[i];
    }

}
