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
public class ApiFiltersDTO {

    private List<String> address;
    private List<String> tagName;
    private List<String> synonimoyu; 
    private List<String> datCollApiContent;
    private LocalDate dateStart;
    private LocalDate dateEnd;
}