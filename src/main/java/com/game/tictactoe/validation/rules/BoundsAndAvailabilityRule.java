package com.game.tictactoe.validation.rules;

import com.game.tictactoe.exception.TicTacToeException;
import com.game.tictactoe.model.GameRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.game.tictactoe.constants.TicTacToeConstants.*;

@Component
@Order(2)
public class BoundsAndAvailabilityRule implements GameValidationRule {

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
        if (PLAYER_X.equalsIgnoreCase(targetSpot) || PLAYER_O.equalsIgnoreCase(targetSpot)) {
            throw TicTacToeException.builder()
                    .message(ERR_MSG_POSITION_TAKEN)
                    .request(request)
                    .build();
        }
    }
}
