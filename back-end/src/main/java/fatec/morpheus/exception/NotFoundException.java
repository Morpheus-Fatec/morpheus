package fatec.morpheus.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import fatec.morpheus.entity.ErrorResponse;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
        private final ErrorResponse errorResponse;
        public NotFoundException(int id) {
            super("News source with id " + id + " not found");
            this.errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND, 
                "News source with id " + id + " not found", 
                new ArrayList<>());
        }
}
