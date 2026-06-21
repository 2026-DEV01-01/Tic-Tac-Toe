package com.game.tictactoe.service;

import com.game.tictactoe.model.GameRequest;
import com.game.tictactoe.model.GameResponse;
import com.game.tictactoe.model.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tic-Tac-Toe Service Tests")
class TicTacToeServiceTest {

    private TicTacToeService service;

    @BeforeEach
    void setUp() {
        service = new TicTacToeService();
    }

    @Test
    @DisplayName("New Game: Board is initialized to 9 slots and X plays first")
    void whenNewGameStarted_thenBoardInitializedAndXGoesFirst() {
        GameResponse response = service.newGame();

        assertEquals(9, response.getBoard().size());
        assertEquals("0", response.getBoard().get(0));
        assertEquals(GameStatus.IN_PROGRESS, response.getStatus());

        GameRequest request = new GameRequest(response.getBoard(), 0);
        response = service.continueGame(request);

        assertEquals("X", response.getBoard().get(0));
        assertEquals(GameStatus.IN_PROGRESS, response.getStatus());
    }

    @Test
    @DisplayName("Play Game: Players alternate between X and O")
    void whenContinuingGame_thenPlayersAlternateTurns() {
        GameResponse response = service.newGame();

        response = service.continueGame(new GameRequest(response.getBoard(), 0)); // X
        response = service.continueGame(new GameRequest(response.getBoard(), 1)); // O
        response = service.continueGame(new GameRequest(response.getBoard(), 2)); // X

        assertEquals("X", response.getBoard().get(0));
        assertEquals("O", response.getBoard().get(1));
        assertEquals("X", response.getBoard().get(2));
    }

    @Test
    @DisplayName("Play Game: Reject attempt to play on an already occupied position")
    void whenPlayingOnPlayedPosition_thenMoveIsRejected() {
        GameResponse response = service.newGame();
        response = service.continueGame(new GameRequest(response.getBoard(), 0)); // X takes 0

        GameResponse invalidResponse = service.continueGame(new GameRequest(response.getBoard(), 0));
        assertEquals(GameStatus.INVALID_MOVE, invalidResponse.getStatus());
        assertEquals("Position already taken! Choose an empty spot.", invalidResponse.getMessage());
        assertEquals("X", invalidResponse.getBoard().get(0)); // Should still be X
    }

    @Test
    @DisplayName("Play Game: Reject attempt to play out of bounds (< 0 or > 8)")
    void whenPlayingOutOfBounds_thenMoveIsRejectedAndTurnNotLost() {
        GameResponse response = service.newGame();
        GameResponse invalidResponse1 = service.continueGame(new GameRequest(response.getBoard(), -1));
        assertEquals(GameStatus.INVALID_MOVE, invalidResponse1.getStatus());

        GameResponse invalidResponse2 = service.continueGame(new GameRequest(response.getBoard(), 9));
        assertEquals(GameStatus.INVALID_MOVE, invalidResponse2.getStatus());

        response = service.continueGame(new GameRequest(response.getBoard(), 0));
        assertEquals("X", response.getBoard().get(0));
    }

    @Test
    @DisplayName("Play Game: Game ends and player wins when three marks align")
    void whenThreeInARow_thenGameEndsAndPlayerWins() {
        GameResponse response = service.newGame();
        response = service.continueGame(new GameRequest(response.getBoard(), 0)); // X
        response = service.continueGame(new GameRequest(response.getBoard(), 3)); // O
        response = service.continueGame(new GameRequest(response.getBoard(), 1)); // X
        response = service.continueGame(new GameRequest(response.getBoard(), 4)); // O
        response = service.continueGame(new GameRequest(response.getBoard(), 2)); // X's winning move
        assertEquals(GameStatus.GAME_OVER_WIN, response.getStatus());
        assertTrue(response.getMessage().contains("Player X wins"));

        GameResponse afterGameOverResponse = service.continueGame(new GameRequest(response.getBoard(), 8));
        assertEquals(GameStatus.GAME_OVER_WIN, afterGameOverResponse.getStatus());
        assertNotEquals("O", afterGameOverResponse.getBoard().get(8)); // Move rejected
    }

    @Test
    @DisplayName("Play Game: Game is a draw when all squares are filled without a winner")
    void whenAllSquaresFilledWithNoWinner_thenGameIsADraw() {
        GameResponse response = service.newGame();
        int[] drawMoves = {0, 1, 2, 4, 3, 5, 7, 6, 8};
        for (int move : drawMoves) {
            response = service.continueGame(new GameRequest(response.getBoard(), move));
        }
        assertEquals(GameStatus.GAME_OVER_DRAW, response.getStatus());
        assertEquals("The game is a Draw!", response.getMessage());
        assertTrue(service.isBoardFull(response.getBoard()));
    }
}