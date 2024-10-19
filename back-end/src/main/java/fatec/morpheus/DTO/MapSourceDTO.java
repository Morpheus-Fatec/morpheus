package fatec.morpheus.DTO;

import fatec.morpheus.entity.MapSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapSourceDTO {
    private String author;
    private String body;
    private String title;    
    private String date;

    public MapSource toEntity() {
        return new MapSource(this);
    }
}

