package com.game.tictactoe.advice;

import com.game.tictactoe.exception.TicTacToeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

import static com.game.tictactoe.constants.TicTacToeConstants.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                VALIDATION_ERROR_DETAIL
        );
        problemDetail.setTitle(VALIDATION_ERROR_TITLE);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        problemDetail.setProperty(ERRORS_PROPERTY_KEY, errors);
        return problemDetail;
    }

    @ExceptionHandler(TicTacToeException.class)
    public ProblemDetail handleTicTacToeException(TicTacToeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problemDetail.setTitle(GAME_ERROR_TITLE);
        if (ex.getGameRequest() != null) {
            problemDetail.setProperty(FAILED_REQUEST_CONTEXT_KEY, ex.getGameRequest());
        }
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllOtherExceptions(Exception ex) throws NoResourceFoundException {
        if (ex instanceof NoResourceFoundException noResourceEx) {
            throw noResourceEx;
        }
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                INTERNAL_ERROR_DETAIL);
    }
}