package com.game.tictactoe.service;

import com.game.tictactoe.factory.GameResponseFactory;
import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.model.GameStatus;
import com.game.tictactoe.ruleengine.TicTacToeRuleEngine;
import com.game.tictactoe.validation.TicTacToeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tic-Tac-Toe Service Orchestration Tests")
class TicTacToeServiceTest {

    @Mock
    private TicTacToeRuleEngine ruleEngine;

    @Mock
    private TicTacToeValidator validator;

    @Mock
    private GameResponseFactory responseFactory;

    @InjectMocks
    private TicTacToeService service;

    private List<String> initialBoard;
    private GameRequest validRequest;

    @BeforeEach
    void setUp() {
        initialBoard = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8");
        validRequest = new GameRequest(initialBoard, 0);
        lenient().when(responseFactory.create(anyList(), any(GameStatus.class), anyString()))
                .thenAnswer(invocation -> {
                    List<String> board = invocation.getArgument(0);
                    GameStatus status = invocation.getArgument(1);
                    String message = invocation.getArgument(2);
                    return new GameResponse()
                            .board(board)
                            .status(status)
                            .message(message);
                });
    }

    @Test
    @DisplayName("New Game: Successfully initializes a new 9-slot board")
    void whenNewGame_thenInitializeBoardAndReturnInProgress() {
        GameResponse response = service.newGame();
        assertNotNull(response);
        assertEquals(9, response.getBoard().size());
        assertEquals("0", response.getBoard().get(0));
        assertEquals("8", response.getBoard().get(8));
        assertEquals(GameStatus.IN_PROGRESS, response.getStatus());
        assertEquals("New game started. X goes first.", response.getMessage());
    }

    @Test
    @DisplayName("Validator fails: Propagates validation exception")
    void givenInvalidMove_whenContinuingGame_thenValidatorHandlesIt() {
        doThrow(new IllegalArgumentException("Invalid move")).when(validator).validate(validRequest);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.continueGame(validRequest);
        });

        assertEquals("Invalid move", exception.getMessage());
        verify(ruleEngine, never()).isGameOver(anyList()); // Ensures orchestration stops immediately
    }

    @Test
    @DisplayName("Game Already Over (Win): Returns GAME_OVER_WIN without making a move")
    void givenAlreadyWonGame_whenContinuingGame_thenReturnGameOverWin() {
        when(ruleEngine.isGameOver(anyList())).thenReturn(true);
        when(ruleEngine.checkWinner(anyList())).thenReturn(true);

        GameResponse response = service.continueGame(validRequest);
        assertEquals(GameStatus.GAME_OVER_WIN, response.getStatus());
        assertEquals("The game is already over.", response.getMessage());
        verify(ruleEngine, never()).determineCurrentPlayer(anyList()); // Ensures no move was made
    }

    @Test
    @DisplayName("Game Already Over (Draw): Returns GAME_OVER_DRAW without making a move")
    void givenAlreadyDrawnGame_whenContinuingGame_thenReturnGameOverDraw() {
        when(ruleEngine.isGameOver(anyList())).thenReturn(true);
        when(ruleEngine.checkWinner(anyList())).thenReturn(false);

        GameResponse response = service.continueGame(validRequest);
        assertEquals(GameStatus.GAME_OVER_DRAW, response.getStatus());
        assertEquals("The game is already over.", response.getMessage());
    }

    @Test
    @DisplayName("Valid Move (Results in Win): Updates board and returns GAME_OVER_WIN")
    void givenWinningMove_whenContinuingGame_thenReturnGameOverWin() {
        when(ruleEngine.isGameOver(anyList())).thenReturn(false);
        when(ruleEngine.determineCurrentPlayer(anyList())).thenReturn("X");
        when(ruleEngine.checkWinner(anyList())).thenReturn(true); // Triggers win after move

        GameResponse response = service.continueGame(validRequest);
        assertEquals(GameStatus.GAME_OVER_WIN, response.getStatus());
        assertEquals("Player X wins the game!", response.getMessage());
        assertEquals("X", response.getBoard().get(0)); // Proves the move was placed
    }

    @Test
    @DisplayName("Valid Move (Results in Draw): Updates board and returns GAME_OVER_DRAW")
    void givenDrawingMove_whenContinuingGame_thenReturnGameOverDraw() {
        when(ruleEngine.isGameOver(anyList())).thenReturn(false);
        when(ruleEngine.determineCurrentPlayer(anyList())).thenReturn("O");
        when(ruleEngine.checkWinner(anyList())).thenReturn(false);
        when(ruleEngine.isBoardFull(anyList())).thenReturn(true); // Triggers draw after move

        GameResponse response = service.continueGame(validRequest);
        assertEquals(GameStatus.GAME_OVER_DRAW, response.getStatus());
        assertEquals("The game is a Draw!", response.getMessage());
        assertEquals("O", response.getBoard().get(0)); // Proves the move was placed
    }

    @Test
    @DisplayName("Valid Move (Game Continues): Updates board, toggles player, and returns IN_PROGRESS")
    void givenStandardMove_whenContinuingGame_thenReturnInProgress() {
        when(ruleEngine.isGameOver(anyList())).thenReturn(false);
        when(ruleEngine.determineCurrentPlayer(anyList())).thenReturn("X");
        when(ruleEngine.checkWinner(anyList())).thenReturn(false);
        when(ruleEngine.isBoardFull(anyList())).thenReturn(false);

        GameResponse response = service.continueGame(validRequest);
        assertEquals(GameStatus.IN_PROGRESS, response.getStatus());
        assertEquals("Move accepted. O's turn.", response.getMessage());
        assertEquals("X", response.getBoard().get(0)); // Proves the move was placed
    }

    @Test
    @DisplayName("Valid Move for Player O (Game Continues): Updates board, toggles player to X, and returns IN_PROGRESS")
    void givenStandardMovePlayerO_whenContinuingGame_thenReturnInProgress() {
        when(ruleEngine.isGameOver(anyList())).thenReturn(false);
        when(ruleEngine.determineCurrentPlayer(anyList())).thenReturn("O");
        when(ruleEngine.checkWinner(anyList())).thenReturn(false);
        when(ruleEngine.isBoardFull(anyList())).thenReturn(false);

        GameResponse response = service.continueGame(validRequest);
        assertEquals(GameStatus.IN_PROGRESS, response.getStatus());
        assertEquals("Move accepted. X's turn.", response.getMessage());
        assertEquals("O", response.getBoard().get(0)); // Proves the move was placed
    }
}