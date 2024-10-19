package fatec.morpheus.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MappingDTO {
    private String author;
    private String body;
    private String title;
    private String url;
    private String date;
}
