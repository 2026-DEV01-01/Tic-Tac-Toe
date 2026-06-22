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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;import org.springframework.validation.BindingResult;import org.springframework.validation.FieldError;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.failedRequestContext.position").value(0));
    }

    @Test
    @DisplayName("Should return 400 ProblemDetail when Business Logic throws TicTacToeException with null request")
    void handleTicTacToeException_WithNullRequest() throws Exception {
        when(gameService.continueGame(any(GameRequest.class)))
                .thenThrow(TicTacToeException.builder()
                        .message("Position already taken!")
                        .request(null)
                        .build());

        GameRequest validRequest = new GameRequest(List.of("0", "1", "2"), 0);
        mockMvc.perform(post(PLAY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Invalid Game Operation"))
                .andExpect(jsonPath("$.detail").value("Position already taken!"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.failedRequestContext").doesNotExist());
    }

    @Test
    @DisplayName("Should return 500 ProblemDetail when an unexpected Exception occurs")
    void handleAllOtherExceptions_ReturnsInternalServerError() throws Exception {
        GameRequest validRequest = new GameRequest(List.of("0", "1", "2"), 0);
        when(gameService.continueGame(any(GameRequest.class)))
                .thenThrow(new NullPointerException("Simulated unexpected system failure"));

        mockMvc.perform(post(PLAY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred processing your request."))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.type").value("about:blank"));
    }

    @Test
    @DisplayName("Should return 400 ProblemDetail when MethodArgumentNotValidException is handled")
    void handleValidationExceptions_ReturnsBadRequest() {
        var handler = new GlobalExceptionHandler();
        var exception = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(BindingResult.class);
        var fieldError = new FieldError("gameRequest", "position", "must be greater than or equal to 0");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        var problemDetail = handler.handleValidationExceptions(exception);

        assertEquals(BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Validation Failed", problemDetail.getTitle());
        assertEquals("The request payload failed structural validation.", problemDetail.getDetail());
        assertEquals(java.net.URI.create("about:blank"), problemDetail.getType());

        var errors = (java.util.List<?>) problemDetail.getProperties().get("errors");
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("must be greater than or equal to 0", errors.get(0));
    }

    @Test
    @DisplayName("Should propagate NoResourceFoundException when handled by handleAllOtherExceptions")
    void handleAllOtherExceptions_PropagatesNoResourceFoundException() {
        var handler = new GlobalExceptionHandler();
        var ex = new NoResourceFoundException(GET, "/not-found");
        assertThrows(NoResourceFoundException.class,
                () -> handler.handleAllOtherExceptions(ex));
    }
}