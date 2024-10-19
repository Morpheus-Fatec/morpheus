package fatec.morpheus.DTO;

import java.util.List;

import fatec.morpheus.entity.MapSource;
import fatec.morpheus.entity.Tag;
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
    private List<Tag> tags;
    private MapSource map;
}
