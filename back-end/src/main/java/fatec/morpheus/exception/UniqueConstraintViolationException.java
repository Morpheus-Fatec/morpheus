package fatec.morpheus.exception;

import fatec.morpheus.entity.ErrorResponse;
import lombok.Getter;

@Getter
public class UniqueConstraintViolationException extends RuntimeException{
    private final ErrorResponse errorResponse;

        public UniqueConstraintViolationException(ErrorResponse errorResponse) {
            super(errorResponse.getMessage());
            this.errorResponse = errorResponse;
            this.errorResponse.setMessage("Duplicate unique value detected");
        }
}
