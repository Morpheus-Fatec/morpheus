package fatec.morpheus.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private int code;
    private String address;
    private String method;
    private String content;

    public ApiResponse(int code, String address, int get, int post){
        if (get == 1 && post == 1) {
            this.method = "Get, Post";
            this.code = code;
            this.address = address;
            return;
        }
        if (get == 1) {
            this.method = "Get";
            this.code = code;
            this.address = address;
            return;
        }
        if (post == 1) {
            this.method = "Post";
            this.code = code;
            this.address = address;
            return;
        }
    }
    

}
