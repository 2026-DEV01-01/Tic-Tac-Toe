package com.game.tictactoe.ruleengine;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.game.tictactoe.constants.TicTacToeConstants.*;

@Component
public class TicTacToeRuleEngine implements GameRuleEngine {

    private final Map<Integer, int[][]> winCombinationsCache = new ConcurrentHashMap<>();

    private int[][] getWinCombinations(int boardSize) {
        return winCombinationsCache.computeIfAbsent(boardSize, this::generateWinCombinations);
    }

    private int[][] generateWinCombinations(int boardSize) {
        var rows = IntStream.range(MIN_INDEX, boardSize)
                .mapToObj(i -> IntStream.range(MIN_INDEX, boardSize).map(j -> i * boardSize + j).toArray());
        var columns = IntStream.range(MIN_INDEX, boardSize)
                .mapToObj(j -> IntStream.range(MIN_INDEX, boardSize).map(i -> i * boardSize + j).toArray());
        var diagonals = Stream.of(
                IntStream.range(MIN_INDEX, boardSize).map(i -> i * boardSize + i).toArray(),
                IntStream.range(MIN_INDEX, boardSize).map(i -> i * boardSize + (boardSize - 1 - i)).toArray()
        );
        return Stream.concat(Stream.concat(rows, columns), diagonals).toArray(int[][]::new);
    }

    @Override
    public boolean checkWinner(List<String> board) {
        int n = (int) Math.sqrt(board.size());
        int[][] combinations = getWinCombinations(n);
        for (var combo : combinations) {
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