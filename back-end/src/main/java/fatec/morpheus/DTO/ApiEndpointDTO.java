package fatec.morpheus.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiEndpointDTO {

    private int code;      
    private String address; 
    private String source;  
    private String method;  

    public void setMethod(int post, int get) {
        if (post == 1) {
            this.method = "POST";
        } else if (get == 1) {
            this.method = "GET";
        }
    }
}
