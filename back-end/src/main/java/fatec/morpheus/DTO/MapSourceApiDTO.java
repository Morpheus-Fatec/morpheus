package fatec.morpheus.DTO;

import fatec.morpheus.entity.MapSourceApi;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapSourceApiDTO {
    private String date;
    private String content;

    public MapSourceApi toEntity(){
        return new MapSourceApi(this);
    }
}
