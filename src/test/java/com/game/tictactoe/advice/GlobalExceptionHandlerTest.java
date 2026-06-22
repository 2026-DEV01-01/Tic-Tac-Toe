package com.game.tictactoe.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.tictactoe.controller.TicTacToeController;
import com.game.tictactoe.exception.TicTacToeException;
import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicTacToeController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GameService gameService;

    private static final String PLAY_ENDPOINT = "/api/tictactoe/play";

    @Test
    @DisplayName("Should return 400 ProblemDetail when Business Logic throws TicTacToeException")
    void handleBusinessLogicExceptions_ReturnsBadRequest() throws Exception {
        GameRequest validRequest = new GameRequest(List.of("0", "1", "2"), 0);

        when(gameService.continueGame(any(GameRequest.class)))
                .thenThrow(TicTacToeException.builder()
                        .message("Position already taken!")
                        .request(validRequest)
                        .build());

        mockMvc.perform(post(PLAY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Invalid Game Operation"))
                .andExpect(jsonPath("$.detail").value("Position already taken!"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.failedRequestContext.position").value(0));
    }

    @Test
    @DisplayName("Should return 500 ProblemDetail when an unexpected Exception occurs")
    void handleAllOtherExceptions_ReturnsInternalServerError() throws Exception {
        GameRequest validRequest = new GameRequest(List.of("0", "1", "2"), 0);
        when(gameService.continueGame(any(GameRequest.class)))
                .thenThrow(new NullPointerException("Database connection lost"));

        mockMvc.perform(post(PLAY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred processing your request."))
                .andExpect(jsonPath("$.status").value(500));
    }
}