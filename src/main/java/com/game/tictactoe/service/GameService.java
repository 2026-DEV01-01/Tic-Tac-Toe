package com.game.tictactoe.service;

import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;

public interface GameService {

    GameResponse newGame();
    GameResponse continueGame(GameRequest request);
}
