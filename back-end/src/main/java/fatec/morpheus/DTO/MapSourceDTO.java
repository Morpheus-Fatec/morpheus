package fatec.morpheus.DTO;

import fatec.morpheus.entity.NewsSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapSourceDTO {
    private NewsSource source;
    private String author; 
    private String body; 
    private String title; 
    private String url;
    private String date;
}
