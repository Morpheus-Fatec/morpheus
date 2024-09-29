package fatec.morpheus.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class NewsSourceException extends RuntimeException {

    public NewsSourceException(String message) {
        super(message);
    }
    @Getter
    public static class NotFoundException extends NewsSourceException {
        private final ErrorResponse errorResponse;
        public NotFoundException(int id) {
            super("News source with id " + id + " not found");
            this.errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, "News source with id " + id + " not found", new ArrayList<>());
        }
    }

    @Getter
    public static class UniqueConstraintViolationException extends NewsSourceException {
        private final ErrorResponse errorResponse;

        public UniqueConstraintViolationException(ErrorResponse errorResponse) {
            super(errorResponse.getMessage());
            this.errorResponse = errorResponse;
            this.errorResponse.setMessage("Duplicate unique value detected");
        }
    
        public ErrorResponse getErrorResponse() {
            return errorResponse;
        }
        
    }

    public static class DatabaseAccessException extends NewsSourceException {
        public DatabaseAccessException(String message) {
            super(message);
        }
    }


}
