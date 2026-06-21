package com.game.tictactoe.ruleengine;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class TicTacToeRuleEngine implements GameRuleEngine{

    private static final Pattern DRAW_PATTERN = Pattern.compile("[XO]");

    private static final int[][] WIN_COMBINATIONS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontal
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Vertical
            {0, 4, 8}, {2, 4, 6}             // Diagonal
    };

    @Override
    public boolean checkWinner(List<String> board) {
        for (int[] combo : WIN_COMBINATIONS) {
            if (board.get(combo[0]).equals(board.get(combo[1])) &&
                    board.get(combo[1]).equals(board.get(combo[2]))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBoardFull(List<String> board) {
        return board.stream().allMatch(cell -> cell != null && DRAW_PATTERN.matcher(cell).matches());
    }

    @Override
    public String determineCurrentPlayer(List<String> board) {
        int xCount = 0;
        int oCount = 0;
        for (String cell : board) {
            if ("X".equals(cell)) xCount++;
            if ("O".equals(cell)) oCount++;
        }
        return (xCount == oCount) ? "X" : "O";
    }

    @Override
    public boolean isGameOver(List<String> board) {
        return checkWinner(board) || isBoardFull(board);
    }
}