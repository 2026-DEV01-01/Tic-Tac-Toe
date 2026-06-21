package com.game.tictactoe.ruleengine;

import java.util.List;

public interface GameRuleEngine {
    boolean checkWinner(List<String> board);
    boolean isBoardFull(List<String> board);
    String determineCurrentPlayer(List<String> board);
    boolean isGameOver(List<String> board);
}
