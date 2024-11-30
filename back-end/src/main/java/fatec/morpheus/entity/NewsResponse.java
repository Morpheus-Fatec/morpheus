package fatec.morpheus.entity;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {

    private String newsTitle;
    private String newsContent;
    private Date newsRegistryDate;
    private String autName;
    private String srcName;
    private String srcAddress;
    private String srcURL;
}
