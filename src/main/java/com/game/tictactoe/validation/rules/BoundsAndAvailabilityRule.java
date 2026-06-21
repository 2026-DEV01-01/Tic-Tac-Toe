package com.game.tictactoe.validation.rules;

import com.game.tictactoe.exception.TicTacToeException;
import com.game.tictactoe.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class BoundsAndAvailabilityRule implements GameValidationRule {

    public static final int MIN_INDEX = 0;
    public static final String ERR_MSG_POSITION_TAKEN = "Position already taken! Choose an empty spot.";
    public static final String ERR_MSG_POSITION_OUT_OF_BOUNDS = "Out of bounds! Please choose a position between 0 and %d.";

    @Override
    public void validate(GameRequest request) {
        var board = request.getBoard();
        var position = request.getPosition();
        int totalSpots = board.size();

        if (position < MIN_INDEX || position >= totalSpots) {
            throw TicTacToeException.builder()
                    .message(ERR_MSG_POSITION_OUT_OF_BOUNDS.formatted(totalSpots - 1))
                    .request(request)
                    .build();
        }

        var targetSpot = board.get(position);
        if ("X".equalsIgnoreCase(targetSpot) || "O".equalsIgnoreCase(targetSpot)) {
            throw TicTacToeException.builder()
                    .message(ERR_MSG_POSITION_TAKEN)
                    .request(request)
                    .build();
        }
    }
}
