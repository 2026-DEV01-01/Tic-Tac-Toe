package com.game.tictactoe.ruleengine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.game.tictactoe.constants.TicTacToeConstants.*;

@Component
public class TicTacToeRuleEngine implements GameRuleEngine {

    private static final int BOARD_DIMENSION = DEFAULT_BOARD_SIZE;
    private static final int[][] WIN_COMBINATIONS = generateWinCombinations(BOARD_DIMENSION);

    private static int[][] generateWinCombinations(int n) {
        var combinations = new ArrayList<int[]>();
        for (int i = 0; i < n; i++) {
            var row = new int[n];
            var col = new int[n];
            for (int j = 0; j < n; j++) {
                row[j] = i * n + j;
                col[j] = j * n + i;
            }
            combinations.add(row);
            combinations.add(col);
        }
        var mainDiagonal = new int[n];
        var antiDiagonal = new int[n];
        for (int i = 0; i < n; i++) {
            mainDiagonal[i] = i * n + i;
            antiDiagonal[i] = i * n + (n - 1 - i);
        }
        combinations.add(mainDiagonal);
        combinations.add(antiDiagonal);
        return combinations.toArray(int[][]::new);
    }

    @Override
    public boolean checkWinner(List<String> board) {
        for (var combo : WIN_COMBINATIONS) {
            var firstSpot = board.get(combo[0]);
            if (PLAYER_X.equals(firstSpot) || PLAYER_O.equals(firstSpot)) {
                boolean isWin = Arrays.stream(combo)
                        .allMatch(index -> firstSpot.equals(board.get(index)));
                if (isWin) return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBoardFull(List<String> board) {
        return board.stream().allMatch(cell -> {
            if (cell == null) return false;
            return switch (cell) {
                case PLAYER_X, PLAYER_O -> true;
                default -> false;
            };
        });
    }

    @Override
    public String determineCurrentPlayer(List<String> board) {
        var xCount = board.stream().filter(PLAYER_X::equals).count();
        var oCount = board.stream().filter(PLAYER_O::equals).count();
        return (xCount == oCount) ? PLAYER_X : PLAYER_O;
    }

    @Override
    public boolean isGameOver(List<String> board) {
        return checkWinner(board) || isBoardFull(board);
    }
}