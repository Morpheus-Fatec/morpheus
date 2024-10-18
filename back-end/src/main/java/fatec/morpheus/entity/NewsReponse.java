package fatec.morpheus.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsReponse {

    private String newsTitle;
    private String newsContent;
    private Date newsRegistryDate;
    private String autName;
    private String srcName;
    private String srcAddress;
}
