package com.game.tictactoe.service;

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
        service.newGame();
    }

    @Test
    @DisplayName("New Game: Board is initialized to 9 slots and X plays first")
    void whenNewGameStarted_thenBoardInitializedAndXGoesFirst() {
        char[] board = service.newGame();
        assertEquals(9, board.length);
        assertEquals('0', board[0]);

        board = service.continueGame(board, 0);
        assertEquals('X', board[0]);
    }

    @Test
    @DisplayName("Play Game: Players alternate between X and O")
    void whenContinuingGame_thenPlayersAlternateTurns() {
        char[] board = service.newGame();
        service.continueGame(board, 0);
        service.continueGame(board, 1);
        service.continueGame(board, 2);

        assertEquals('X', board[0]);
        assertEquals('O', board[1]);
        assertEquals('X', board[2]);
    }

    @Test
    @DisplayName("Play Game: Reject attempt to play on an already occupied position")
    void whenPlayingOnPlayedPosition_thenMoveIsRejected() {
        char[] board = service.newGame();
        service.continueGame(board, 0);
        service.continueGame(board, 0);
        assertEquals('X', board[0]);

        service.continueGame(board, 1);
        assertEquals('O', board[1]);
    }
}