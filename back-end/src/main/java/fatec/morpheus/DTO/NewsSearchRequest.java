package fatec.morpheus.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsSearchRequest {
    private List<String> textSearch;      
    private List<String> sourcesOrigin;    
    private List<String> titleSearch;      
    private LocalDate dateStart;           
    private LocalDate dateEnd;             
    private List<String> author;           

}

