package fatec.morpheus.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsSourceDTO {
    private String srcName;
    private String type;
    private String address;
    private List<Integer> tagCodes;
    private MapSourceDTO map;
}
