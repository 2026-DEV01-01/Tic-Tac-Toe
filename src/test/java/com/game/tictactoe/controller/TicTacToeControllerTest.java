package com.game.tictactoe.controller;

import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.model.GameStatus;
import com.game.tictactoe.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tic-Tac-Toe Controller Tests")
class TicTacToeControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private TicTacToeController controller;

    private GameResponse mockResponse;
    private GameRequest mockRequest;

    @BeforeEach
    void setUp() {
        List<String> mockBoard = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8");

        mockResponse = new GameResponse()
                .board(mockBoard)
                .status(GameStatus.IN_PROGRESS)
                .message("Test message");

        mockRequest = new GameRequest(mockBoard, 0);
    }

    @Test
    @DisplayName("Init Game: Returns 200 OK and valid GameResponse")
    void whenInitGame_thenReturnOkResponseEntity() {
        when(gameService.newGame(3)).thenReturn(mockResponse);
        ResponseEntity<GameResponse> responseEntity = controller.initGame(3);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        verify(gameService, times(1)).newGame(3);
    }

    @Test
    @DisplayName("Play Move: Returns 200 OK and delegates to GameService")
    void givenValidRequest_whenPlayMove_thenReturnOkResponseEntity() {
        when(gameService.continueGame(any(GameRequest.class))).thenReturn(mockResponse);
        ResponseEntity<GameResponse> responseEntity = controller.playMove(mockRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        verify(gameService, times(1)).continueGame(mockRequest);
    }
}