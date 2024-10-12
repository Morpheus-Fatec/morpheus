package fatec.morpheus.DTO;

import fatec.morpheus.entity.NewsSource;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapSourceDTO {
    @NotNull
    private NewsSource source;
    
    private String author;
    
    @NotNull
    private String body;
    
    @NotNull
    private String title;
    
    @NotNull
    private String url;
    
    @NotNull
    private String date;
}

