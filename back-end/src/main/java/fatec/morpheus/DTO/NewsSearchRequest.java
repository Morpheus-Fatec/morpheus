package fatec.morpheus.DTO;

import java.time.LocalDate;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsSearchRequest {  
    private List<Integer> sourcesOrigin; 
    private List<Integer> author;  
    private List<String> titleSearch;  
    private List<String> textSearch;    
    private LocalDate dateStart;           
    private LocalDate dateEnd;             
  
}

