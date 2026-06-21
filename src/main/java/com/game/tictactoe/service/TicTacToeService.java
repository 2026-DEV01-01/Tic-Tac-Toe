package com.game.tictactoe.service;

import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.model.GameStatus;
import com.game.tictactoe.ruleengine.GameRuleEngine;
import com.game.tictactoe.validation.GameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TicTacToeService {

    public static final String GAME_INIT_MESSAGE = "New game started. X goes first.";
    public static final String GAME_FINISHED_MESSAGE = "The game is already over.";
    public static final String GAME_WIN_MESSAGE = "Player %s wins the game!";
    public static final String GAME_DRAW_MESSAGE = "The game is a Draw!";
    public static final String GAME_MOVE_ACCEPTED_MESSAGE = "Move accepted. %s's turn.";

    private final GameRuleEngine ruleEngine;
    private final GameValidator validator;

    public GameResponse newGame() {
        List<String> initialBoard = IntStream.range(0, 9)
                .mapToObj(String::valueOf)
                .toList();
        return buildResponse(initialBoard, GameStatus.IN_PROGRESS, GAME_INIT_MESSAGE);
    }

    public GameResponse continueGame(GameRequest request) {
        validator.validate(request);
        List<String> board = new ArrayList<>(request.getBoard());
        int position = request.getPosition();

        if (ruleEngine.isGameOver(board)) {
            GameStatus currentStatus = ruleEngine.checkWinner(board) ? GameStatus.GAME_OVER_WIN : GameStatus.GAME_OVER_DRAW;
            return buildResponse(board, currentStatus, GAME_FINISHED_MESSAGE);
        }

        String currentPlayer = ruleEngine.determineCurrentPlayer(board);
        board.set(position, currentPlayer);
        if (ruleEngine.checkWinner(board)) {
            return buildResponse(board, GameStatus.GAME_OVER_WIN, GAME_WIN_MESSAGE.formatted(currentPlayer));
        } else if (ruleEngine.isBoardFull(board)) {
            return buildResponse(board, GameStatus.GAME_OVER_DRAW, GAME_DRAW_MESSAGE);
        }
        String nextPlayer = "X".equals(currentPlayer) ? "O" : "X";
        return buildResponse(board, GameStatus.IN_PROGRESS, GAME_MOVE_ACCEPTED_MESSAGE.formatted(nextPlayer));
    }

    private GameResponse buildResponse(List<String> board, GameStatus status, String message) {
        return GameResponse.builder()
                .board(board)
                .status(status)
                .message(message)
                .build();
    }
}