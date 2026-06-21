package com.game.tictactoe.factory;

import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.model.GameStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameResponseFactory {

    public GameResponse create(List<String> board,
                               GameStatus status,
                               String message) {
        return new GameResponse()
                .board(board)
                .status(status)
                .message(message);
    }
}