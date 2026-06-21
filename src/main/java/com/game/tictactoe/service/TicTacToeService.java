package com.game.tictactoe.service;

public class TicTacToeService {

    private static boolean gameRunning = false;
    private static char currentPlayer;

    public char[] newGame() {
        char[] board = new char[9];
        initializeBoard(board);
        gameRunning = true;
        currentPlayer = 'X';
        return board;
    }

    public char[] continueGame(char[] board, int position) {
        if (gameRunning) {
            if (inputValidator(board, position)) {
                board[position] = currentPlayer;
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }
        return board;
    }

    private void initializeBoard(char[] board) {
        for (int i = 0; i < 9; i++) {
            board[i] = (char) ('0' + i);
        }
    }

    private boolean inputValidator(char[] board, int position) {
        if (position < 0 || position > 8) {
            System.out.println("Out of bounds!");
            return false;
        } else if (board[position] == 'X' || board[position] == 'O') {
            System.out.println("Position already taken!");
            return false;
        }
        return true;
    }
}