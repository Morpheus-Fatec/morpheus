package fatec.morpheus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import fatec.morpheus.exception.NewsSourceException.DatabaseAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NewsSourceException.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NewsSourceException.NotFoundException ex) { 
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NewsSourceException.UniqueConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleUniqueConstraint(NewsSourceException.UniqueConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NewsSourceException.DatabaseAccessException.class)
    public ResponseEntity<String> handleDatabaseAccess(DatabaseAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

