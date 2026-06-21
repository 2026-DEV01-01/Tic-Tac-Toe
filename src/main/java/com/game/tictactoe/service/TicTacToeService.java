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
                if (checkWinner(board)) {
                    System.out.println("Player " + currentPlayer + " wins the game!");
                    gameRunning = false;
                } else {
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
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

    private boolean checkWinner(char[] board) {
        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontal
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Vertical
                {0, 4, 8}, {2, 4, 6}             // Diagonal
        };

        for (int[] combo : winCombinations) {
            if (board[combo[0]] == board[combo[1]] && board[combo[1]] == board[combo[2]]) {
                return true;
            }
        }
        return false;
    }
}