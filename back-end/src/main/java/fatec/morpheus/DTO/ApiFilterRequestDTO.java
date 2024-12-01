package fatec.morpheus.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ApiFilterRequestDTO {
    private List<String> address;
    private List<String> text;
    private List<String> tags;
    private LocalDate dateStart;
    private LocalDate dateEnd;
}
