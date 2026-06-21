package com.game.tictactoe.validation.rules;


import com.game.tictactoe.model.GameRequest;

public interface GameValidationRule {
    void validate(GameRequest request);
}