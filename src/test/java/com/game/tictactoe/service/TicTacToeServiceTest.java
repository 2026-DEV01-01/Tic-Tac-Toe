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
}