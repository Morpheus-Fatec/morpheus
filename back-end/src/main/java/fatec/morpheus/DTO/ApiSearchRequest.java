package fatec.morpheus.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiSearchRequest {

    private List<String> address;
    private List<String> text;
    private List<String> tags;
    private LocalDate dateStart;           
    private LocalDate dateEnd;
    private int totalPages;  
    private long totalElements;   

}
