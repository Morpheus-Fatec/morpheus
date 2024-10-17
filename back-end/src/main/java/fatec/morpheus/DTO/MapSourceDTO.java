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
    @NotBlank(message = "O corpo não deve estar em branco.")
    @NotNull(message = "O corpo não deve ser nulo.")
    private String body;
    
    @NotBlank(message = "O título não deve estar em branco.")
    @NotNull(message = "O título não deve ser nulo.")
    private String title;
    
    @NotBlank(message = "A URL não deve estar em branco.")
    @NotNull(message = "A URL não deve ser nula.")
    private String url;
    
    @NotBlank(message = "A data não deve estar em branco.")
    @NotNull(message = "A data não deve ser nula.")
    private String date;


    public MapSource toEntity() {
        return new MapSource(this);
    }
}

