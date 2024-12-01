package fatec.morpheus.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class TagDTO {
    private Integer id;
    private String name;
    private Integer newsCount;
}
