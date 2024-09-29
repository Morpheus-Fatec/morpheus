package fatec.morpheus.exception;

import lombok.Getter;

public class NewsSourceException extends RuntimeException {

    public NewsSourceException(String message) {
        super(message);
    }

    public static class NotFoundException extends NewsSourceException {
        public NotFoundException(int id) {
            super("News source not found with ID: " + id);
        }
    }

    @Getter
    public static class UniqueConstraintViolationException extends NewsSourceException {
        private final ErrorResponse errorResponse;

        public UniqueConstraintViolationException(ErrorResponse errorResponse) {
            super(errorResponse.getMessage());
            this.errorResponse = errorResponse;
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
