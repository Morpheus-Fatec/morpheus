package fatec.morpheus.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsSourceDTO {
    private String name;
    private String type;
    private String address;
    private MapSourceDTO map;
}
