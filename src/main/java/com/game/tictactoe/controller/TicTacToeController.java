package com.game.tictactoe.controller;

import com.game.tictactoe.api.GameApi;
import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicTacToeController implements GameApi {

    private final GameService gameService;

    @Override
    public ResponseEntity<GameResponse> initGame(Integer size) {
        GameResponse response = gameService.newGame(size);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GameResponse> playMove(@Valid GameRequest gameRequest) {
        return ResponseEntity.ok(gameService.continueGame(gameRequest));
    }
}
