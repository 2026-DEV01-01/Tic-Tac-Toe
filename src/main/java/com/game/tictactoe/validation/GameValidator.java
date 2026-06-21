package com.game.tictactoe.validation;

import com.game.tictactoe.model.GameRequest;

public interface GameValidator {
    void validate(GameRequest request);
}
