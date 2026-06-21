package com.game.tictactoe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicTacToeService {

    public char[] newGame() {
        char[] board = new char[9];
        initializeBoard(board);
        return board;
    }

    public char[] continueGame(char[] board, int position) {
        if (isGameOver(board)) {
            return board;
        }
        char currentPlayer = determineCurrentPlayer(board);
        if (inputValidator(board, position)) {
            board[position] = currentPlayer;
            if (checkWinner(board)) {
                System.out.println("Player " + currentPlayer + " wins the game!");
            } else if (isBoardFull(board)) {
                System.out.println("The game is a Draw!");
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
            System.out.println("Out of bounds! Please choose a position between 0 and 8.");
            return false;
        }
        if (board[position] == 'X' || board[position] == 'O') {
            System.out.println("Position already taken! Choose an empty spot.");
            return false;
        }
        return true;
    }

    private boolean checkWinner(char[] board) {
        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] combo : winCombinations) {
            if (board[combo[0]] == board[combo[1]] && board[combo[1]] == board[combo[2]]) {
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull(char[] board) {
        for (int i = 0; i < 9; i++) {
            if (board[i] != 'X' && board[i] != 'O') return false;
        }
        return true;
    }

    private char determineCurrentPlayer(char[] board) {
        int xCount = 0;
        int oCount = 0;
        for (char c : board) {
            if (c == 'X') xCount++;
            if (c == 'O') oCount++;
        }
        return (xCount == oCount) ? 'X' : 'O';
    }

    private boolean isGameOver(char[] board) {
        return checkWinner(board) || isBoardFull(board);
    }
}