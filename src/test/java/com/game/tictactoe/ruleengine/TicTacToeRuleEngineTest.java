package com.game.tictactoe.ruleengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tic-Tac-Toe Rule Engine Tests")
class TicTacToeRuleEngineTest {

    private TicTacToeRuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        ruleEngine = new TicTacToeRuleEngine();
    }

    @Test
    @DisplayName("Rules: Detects a winning combination")
    void givenWinningBoard_whenCheckingWinner_thenReturnTrue() {
        List<String> board = Arrays.asList("X", "X", "X", "3", "4", "5", "6", "7", "8");
        assertTrue(ruleEngine.checkWinner(board));
    }

    @Test
    @DisplayName("Rules: Correctly identifies a full board")
    void givenFullBoard_whenCheckingIfFull_thenReturnTrue() {
        List<String> board = Arrays.asList("X", "O", "X", "X", "O", "O", "O", "X", "X");
        assertTrue(ruleEngine.isBoardFull(board));
    }

    @Test
    @DisplayName("Rules: Determines current player accurately based on move counts")
    void givenBoardState_whenDeterminingPlayer_thenReturnCorrectPlayer() {
        List<String> emptyBoard = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8");
        List<String> oneMoveBoard = Arrays.asList("X", "1", "2", "3", "4", "5", "6", "7", "8");

        assertEquals("X", ruleEngine.determineCurrentPlayer(emptyBoard));
        assertEquals("O", ruleEngine.determineCurrentPlayer(oneMoveBoard));
    }

    @Test
    @DisplayName("Game Over: Returns true when a player has won (board not full)")
    void givenWinningBoard_whenCheckingIfGameOver_thenReturnTrue() {
        List<String> board = Arrays.asList("X", "X", "X", "3", "4", "5", "6", "7", "8");
        assertTrue(ruleEngine.isGameOver(board), "Game should be over because a player won");
    }

    @Test
    @DisplayName("Game Over: Returns true when the game is a draw (board is full)")
    void givenFullBoardWithNoWinner_whenCheckingIfGameOver_thenReturnTrue() {
        List<String> board = Arrays.asList("X", "O", "X", "X", "O", "O", "O", "X", "X");
        assertTrue(ruleEngine.isGameOver(board), "Game should be over because the board is full (Draw)");
    }

    @Test
    @DisplayName("Game Over: Returns true when a player wins on the final move (board is full)")
    void givenFullBoardWithWinner_whenCheckingIfGameOver_thenReturnTrue() {
        List<String> board = Arrays.asList("X", "O", "O", "O", "X", "X", "O", "X", "X");
        assertTrue(ruleEngine.isGameOver(board), "Game should be over because a player won on the last turn");
    }

    @Test
    @DisplayName("Game Over: Returns false when the game is still in progress")
    void givenInProgressBoard_whenCheckingIfGameOver_thenReturnFalse() {
        List<String> board = Arrays.asList("X", "O", "2", "3", "4", "5", "6", "7", "8");
        assertFalse(ruleEngine.isGameOver(board), "Game should not be over; it is still in progress");
    }
}
