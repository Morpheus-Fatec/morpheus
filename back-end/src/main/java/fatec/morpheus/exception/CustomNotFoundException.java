package fatec.morpheus.exception;

import fatec.morpheus.DTO.ErrorResponse;

public class CustomNotFoundException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public CustomNotFoundException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}

