package com.dadm.retocero.dto;

import java.util.Random;

public class TicTacToeGame {

    public static final int BOARD_SIZE = 9;
    private char mBoard[] = new char[BOARD_SIZE];

    // Characters used to represent the human, computer, and open spots
    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';

    private Random mRand;

    public TicTacToeGame() {
        // Seed the random number generator
        mRand = new Random();
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
    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    public int getComputerMove(){
        int move = -1;
        for(int i = 0; i < BOARD_SIZE; i++){
            if(mBoard[i] != HUMAN_PLAYER && mBoard[i]!= COMPUTER_PLAYER){
                char current = mBoard[i];
                // First check if there's a move the computer can make to win
                mBoard[i] = COMPUTER_PLAYER;
                if(checkForWinner() == 3){
                    return i;
                }
                // Check if there's a move the computer can make to block the human player from winning
                mBoard[i] = HUMAN_PLAYER;
                if(checkForWinner() == 2){
                    move = i;
                }
                mBoard[i] = current;
            }
        }

        // If no winning or blocking move was found, make a random move
        if(move == -1){
            do{
                move = mRand.nextInt(BOARD_SIZE);
            }while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);
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

        // Check for each winning combination
        for (int[] combination : winCombinations) {
            if (mBoard[combination[0]] == HUMAN_PLAYER && mBoard[combination[1]] == HUMAN_PLAYER && mBoard[combination[2]] == HUMAN_PLAYER)
                return 2;
            if (mBoard[combination[0]] == COMPUTER_PLAYER && mBoard[combination[1]] == COMPUTER_PLAYER && mBoard[combination[2]] == COMPUTER_PLAYER)
                return 3;
        }

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        return 1;

    }

}
