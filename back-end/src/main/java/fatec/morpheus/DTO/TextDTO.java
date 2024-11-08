package fatec.morpheus.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TextDTO {
    private String content;
    private Integer code;
    private List<Integer> synonyms;
}