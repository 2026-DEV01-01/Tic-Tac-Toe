package com.game.tictactoe.service;

import com.game.tictactoe.factory.GameResponseFactory;import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.model.GameStatus;
import com.game.tictactoe.ruleengine.GameRuleEngine;
import com.game.tictactoe.validation.GameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.game.tictactoe.constants.TicTacToeConstants.*;

@Service
@RequiredArgsConstructor
public class TicTacToeService {

    private final GameRuleEngine ruleEngine;
    private final GameValidator validator;
    private final GameResponseFactory factory;

    public GameResponse newGame() {
        List<String> initialBoard = IntStream.range(MIN_INDEX, DEFAULT_ARRAY_SIZE)
                .mapToObj(String::valueOf)
                .toList();
        return factory.create(initialBoard, GameStatus.IN_PROGRESS, GAME_INIT_MESSAGE);
    }

    public GameResponse continueGame(GameRequest request) {
        validator.validate(request);
        List<String> board = new ArrayList<>(request.getBoard());
        int position = request.getPosition();

        if (ruleEngine.isGameOver(board)) {
            GameStatus currentStatus = ruleEngine.checkWinner(board) ? GameStatus.GAME_OVER_WIN : GameStatus.GAME_OVER_DRAW;
            return factory.create(board, currentStatus, GAME_FINISHED_MESSAGE);
        }

        String currentPlayer = ruleEngine.determineCurrentPlayer(board);
        board.set(position, currentPlayer);
        if (ruleEngine.checkWinner(board)) {
            return factory.create(board, GameStatus.GAME_OVER_WIN, GAME_WIN_MESSAGE.formatted(currentPlayer));
        } else if (ruleEngine.isBoardFull(board)) {
            return factory.create(board, GameStatus.GAME_OVER_DRAW, GAME_DRAW_MESSAGE);
        }
        String nextPlayer = "X".equals(currentPlayer) ? "O" : "X";
        return factory.create(board, GameStatus.IN_PROGRESS, GAME_MOVE_ACCEPTED_MESSAGE.formatted(nextPlayer));
    }
}