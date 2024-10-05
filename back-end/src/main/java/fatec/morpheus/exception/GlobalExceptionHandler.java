package fatec.morpheus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import fatec.morpheus.entity.ErrorResponse;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) { 
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

}

