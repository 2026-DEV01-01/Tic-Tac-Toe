package com.game.tictactoe.validation.rules;

import com.game.tictactoe.exception.TicTacToeException;
import com.game.tictactoe.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(1)
public class BoardStateRule implements GameValidationRule {

    public static final int DEFAULT_ARRAY_SIZE = 9;
    public static final String ERR_MSG_NULL_BOARD_STATE = "Invalid board state. Board cannot be null.";
    public static final String ERR_MSG_NON_SQUARE_BOARD = "Invalid board state. Array size must be a perfect square (e.g., 9, 16).";

    @Override
    public void validate(GameRequest request) {
        if (Objects.isNull(request.getBoard())) {
            throw TicTacToeException.builder()
                    .message(ERR_MSG_NULL_BOARD_STATE)
                    .request(request)
                    .build();
        }

        int totalSpots = request.getBoard().size();
        int boardSize = (int) Math.sqrt(totalSpots);

        if (boardSize * boardSize != totalSpots || totalSpots < DEFAULT_ARRAY_SIZE) {
            throw TicTacToeException.builder()
                    .message(ERR_MSG_NON_SQUARE_BOARD)
                    .request(request)
                    .build();
        }
    }
}