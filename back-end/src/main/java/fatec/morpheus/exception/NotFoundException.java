package fatec.morpheus.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import fatec.morpheus.entity.ErrorResponse;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
        private final ErrorResponse errorResponse;
        public NotFoundException(int id, String message) {
            this.errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND, 
                message + " com id " + id, 
                new ArrayList<>());
        }

        public NotFoundException(String name, String message) {
            this.errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND, 
                message + " com palavra " + name, 
                new ArrayList<>());
        }
}
