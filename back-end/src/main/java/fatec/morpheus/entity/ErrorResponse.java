package fatec.morpheus.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ErrorResponse(HttpStatus status, List<String> errors) {
        this.status = status;
        this.errors = errors;
    }

    public ErrorResponse(HttpStatus status, List<String> errors, String message) {
        this.status = status;
        this.errors = errors;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.errors = new ArrayList<>();
        this.message = message;
    }


}
