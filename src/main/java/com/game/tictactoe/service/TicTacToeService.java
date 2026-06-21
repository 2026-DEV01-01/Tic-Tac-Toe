package com.game.tictactoe.service;

import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.model.GameStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicTacToeService implements GameService{

    @Override
    public GameResponse newGame() {
        List<String> board = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            board.add(String.valueOf(i));
        }

        return GameResponse.builder()
                .board(board)
                .status(GameStatus.IN_PROGRESS)
                .message("New game started. X goes first.")
                .build();
    }

    @Override
    public GameResponse continueGame(GameRequest request) {
        List<String> board = new ArrayList<>(request.getBoard());
        int position = request.getPosition();
        if (isGameOver(board)) {
            GameStatus currentStatus = checkWinner(board) ? GameStatus.GAME_OVER_WIN : GameStatus.GAME_OVER_DRAW;
            return GameResponse.builder()
                    .board(board)
                    .status(currentStatus)
                    .message("The game is already over.")
                    .build();
        }
        String validationError = validateInput(board, position);
        if (validationError != null) {
            return GameResponse.builder()
                    .board(board)
                    .status(GameStatus.INVALID_MOVE)
                    .message(validationError)
                    .build();
        }
        String currentPlayer = determineCurrentPlayer(board);
        board.set(position, currentPlayer);
        if (checkWinner(board)) {
            return GameResponse.builder()
                    .board(board)
                    .status(GameStatus.GAME_OVER_WIN)
                    .message("Player " + currentPlayer + " wins the game!")
                    .build();
        } else if (isBoardFull(board)) {
            return GameResponse.builder()
                    .board(board)
                    .status(GameStatus.GAME_OVER_DRAW)
                    .message("The game is a Draw!")
                    .build();
        }
        String nextPlayer = "X".equals(currentPlayer) ? "O" : "X";
        return GameResponse.builder()
                .board(board)
                .status(GameStatus.IN_PROGRESS)
                .message("Move accepted. " + nextPlayer + "'s turn.")
                .build();
    }

    private String validateInput(List<String> board, int position) {
        if (position < 0 || position > 8) {
            return "Out of bounds! Please choose a position between 0 and 8.";
        }
        String spot = board.get(position);
        if ("X".equals(spot) || "O".equals(spot)) {
            return "Position already taken! Choose an empty spot.";
        }
        return null;
    }

    private boolean checkWinner(List<String> board) {
        int[][] winCombinations = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] combo : winCombinations) {
            if (board.get(combo[0]).equals(board.get(combo[1])) &&
                    board.get(combo[1]).equals(board.get(combo[2]))) {
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull(List<String> board) {
        for (String cell : board) {
            if (!"X".equals(cell) && !"O".equals(cell)) {
                return false;
            }
        }
        return true;
    }

    private String determineCurrentPlayer(List<String> board) {
        int xCount = 0;
        int oCount = 0;
        for (String cell : board) {
            if ("X".equals(cell)) xCount++;
            if ("O".equals(cell)) oCount++;
        }
        return (xCount == oCount) ? "X" : "O";
    }

    private boolean isGameOver(List<String> board) {
        return checkWinner(board) || isBoardFull(board);
    }
}