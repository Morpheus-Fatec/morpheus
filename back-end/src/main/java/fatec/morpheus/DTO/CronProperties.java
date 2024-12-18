package fatec.morpheus.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CronProperties {
    private String frequency;
    private String time;
    private String timeZone;
    private boolean active;
    private String timeout;
}
