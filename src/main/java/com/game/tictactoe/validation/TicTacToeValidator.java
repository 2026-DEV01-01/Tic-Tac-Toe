package com.game.tictactoe.validation;


import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.validation.rules.GameValidationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicTacToeValidator implements GameValidator {

    private final List<GameValidationRule> validationRules;

    @Override
    public void validate(GameRequest request) {
        for (var rule : validationRules) {
            rule.validate(request);
        }
    }
}