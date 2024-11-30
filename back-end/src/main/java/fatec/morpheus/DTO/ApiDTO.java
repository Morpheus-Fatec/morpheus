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
public class ApiDTO {
    private String address;
    private int get;
    private int post;
    private List<Integer> tagCodes;
}
