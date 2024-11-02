package fatec.morpheus.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsSearchRequest {  
    private int[] sourcesOrigin; 
    private int[] author;  
    private List<String> titleSearch;  
    private List<String> textSearch;    
    private LocalDate dateStart;           
    private LocalDate dateEnd;             
  
}

