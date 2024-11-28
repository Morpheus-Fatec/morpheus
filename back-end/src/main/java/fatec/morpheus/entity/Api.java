package fatec.morpheus.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Api {
    private int code;
    private String name;
    private String address;
    private String content;
    private String apiRegistryDate;
    private List<Tag> tags;
    private String method;

}
