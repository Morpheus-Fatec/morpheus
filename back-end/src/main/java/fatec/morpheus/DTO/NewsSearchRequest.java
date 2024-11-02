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
public class NewsSearchRequest {
    private List<String> textSearch;      
    private List<Integer> sourcesOrigin;    
    private List<String> titleSearch;      
    private LocalDate dateStart;           
    private LocalDate dateEnd;             
    private List<Integer> author;
    private int totalPages;
    private long totalElements;           

}

