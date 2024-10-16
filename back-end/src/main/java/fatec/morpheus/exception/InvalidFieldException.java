package fatec.morpheus.exception;

import fatec.morpheus.entity.ErrorResponse;

public class InvalidFieldException extends RuntimeException{
    private final ErrorResponse errorResponse;

    public InvalidFieldException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
        this.errorResponse.setMessage("Invalid field");
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

}
