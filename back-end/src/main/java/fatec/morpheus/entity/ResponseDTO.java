package fatec.morpheus.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDTO {
    private String title;
    private String url;
    private String content;
    private String date;

    public ResponseDTO(String title, String url) {
        this.title = title;
        this.url = url;
    }
}