package fatec.morpheus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import fatec.morpheus.DTO.ErrorResponse;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) { 
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomNotFound(CustomNotFoundException ex) { 
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleUniqueConstraint(UniqueConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFieldException(InvalidFieldException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "O JSON enviado está malformado ou contém valores inválidos.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAuthorsFoundException.class)
    public ResponseEntity<String> handleNoAuthorsFoundException(NoAuthorsFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

