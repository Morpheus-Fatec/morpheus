package fatec.morpheus.DTO;

import fatec.morpheus.entity.MapSource;
import jakarta.validation.constraints.NotBlank;
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
    private String author;
    private String body;
    private String title;    
    private String date;

    public MapSource toEntity() {
        return new MapSource(this);
    }
}

