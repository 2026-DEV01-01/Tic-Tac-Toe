package com.game.tictactoe.validation.rules;

import com.game.tictactoe.exception.TicTacToeException;
import com.game.tictactoe.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.game.tictactoe.constants.TicTacToeConstants.*;

@Component
@Order(1)
public class BoardStateRule implements GameValidationRule {


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

        boolean hasInvalidCells = request.getBoard().stream()
                .anyMatch(cell -> cell == null || !cell.matches("\\d+|[XO]"));
        if (hasInvalidCells) {
            throw TicTacToeException.builder()
                    .message(ERR_MSG_INVALID_BOARD_CHARACTERS)
                    .request(request)
                    .build();
        }
    }
}