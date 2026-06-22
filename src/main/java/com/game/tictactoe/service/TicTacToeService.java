package com.game.tictactoe.service;

import com.game.tictactoe.factory.GameResponseFactory;
import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.model.GameStatus;
import com.game.tictactoe.ruleengine.GameRuleEngine;
import com.game.tictactoe.validation.GameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static com.game.tictactoe.constants.TicTacToeConstants.*;

@Service
@RequiredArgsConstructor
public class TicTacToeService implements GameService {

    private final GameRuleEngine ruleEngine;
    private final GameValidator validator;
    private final GameResponseFactory factory;

    @Override
    public GameResponse newGame(Integer size) {
        int boardSize = Math.max(size != null ? size : DEFAULT_BOARD_SIZE, DEFAULT_BOARD_SIZE);
        int totalSpots = boardSize * boardSize;
        var initialBoard = IntStream.range(MIN_INDEX, totalSpots)
                .mapToObj(String::valueOf)
                .toList();
        return factory.create(initialBoard, GameStatus.IN_PROGRESS, GAME_INIT_MESSAGE);
    }

    @Override
    public GameResponse continueGame(GameRequest request) {
        validator.validate(request);
        var board = new ArrayList<>(request.getBoard());
        var position = request.getPosition();
        if (ruleEngine.isGameOver(board)) {
            var currentStatus = ruleEngine.checkWinner(board)
                    ? GameStatus.GAME_OVER_WIN
                    : GameStatus.GAME_OVER_DRAW;
            return factory.create(board, currentStatus, GAME_FINISHED_MESSAGE);
        }
        var currentPlayer = ruleEngine.determineCurrentPlayer(board);
        board.set(position, currentPlayer);
        if (ruleEngine.checkWinner(board)) {
            return factory.create(board, GameStatus.GAME_OVER_WIN, GAME_WIN_MESSAGE.formatted(currentPlayer));
        }
        if (ruleEngine.isBoardFull(board)) {
            return factory.create(board, GameStatus.GAME_OVER_DRAW, GAME_DRAW_MESSAGE);
        }
        var nextPlayer = PLAYER_X.equals(currentPlayer) ? PLAYER_O : PLAYER_X;
        return factory.create(board, GameStatus.IN_PROGRESS, GAME_MOVE_ACCEPTED_MESSAGE.formatted(nextPlayer));
    }
}