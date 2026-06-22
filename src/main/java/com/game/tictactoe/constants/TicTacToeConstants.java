package com.game.tictactoe.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TicTacToeConstants {
    public static final int DEFAULT_ARRAY_SIZE = 9;
    public static final int DEFAULT_BOARD_SIZE = 3;
    public static final int MIN_INDEX = 0;

    public static final String PLAYER_X = "X";
    public static final String PLAYER_O = "O";

    public static final String GAME_INIT_MESSAGE = "New game started. X goes first.";
    public static final String GAME_FINISHED_MESSAGE = "The game is already over.";
    public static final String GAME_WIN_MESSAGE = "Player %s wins the game!";
    public static final String GAME_DRAW_MESSAGE = "The game is a Draw!";
    public static final String GAME_MOVE_ACCEPTED_MESSAGE = "Move accepted. %s's turn.";

    public static final String ERR_MSG_NULL_BOARD_STATE = "Invalid board state. Board cannot be null.";
    public static final String ERR_MSG_NON_SQUARE_BOARD = "Invalid board state. Array size must be a perfect square (e.g., 9, 16).";
    public static final String ERR_MSG_INVALID_BOARD_CHARACTERS = "Invalid board state. only integers 0-9, X, or O are permitted.";

    public static final String ERR_MSG_POSITION_TAKEN = "Position already taken! Choose an empty spot.";
    public static final String ERR_MSG_POSITION_OUT_OF_BOUNDS = "Out of bounds! Please choose a position between 0 and %d.";

    public static final String VALIDATION_ERROR_TITLE = "Validation Failed";
    public static final String VALIDATION_ERROR_DETAIL = "The request payload failed structural validation.";
    public static final String ERRORS_PROPERTY_KEY = "errors";

    public static final String GAME_ERROR_TITLE = "Invalid Game Operation";
    public static final String FAILED_REQUEST_CONTEXT_KEY = "failedRequestContext";

    public static final String INTERNAL_ERROR_TITLE = "Internal Server Error";
    public static final String INTERNAL_ERROR_DETAIL = "An unexpected error occurred processing your request.";
}
