package fatec.morpheus.entity;

import java.sql.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsPortal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    private Date registrationDate;
    // private List<Tag> tags;

    
    @Override
    public String toString() {
        return "NewsPortal [id=" + id + ", name=" + name + ", link=" + link + ", registrationDate=" + registrationDate
                + "]";
    }

    
}

