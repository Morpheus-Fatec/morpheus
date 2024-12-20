package fatec.morpheus.exception;

import fatec.morpheus.DTO.ErrorResponse;

public class InvalidFieldException extends RuntimeException{
    private final ErrorResponse errorResponse;

    public InvalidFieldException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

}
