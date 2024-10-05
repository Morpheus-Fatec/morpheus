package fatec.morpheus.entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mapId; 

    @ManyToOne 
    @JoinColumn(name = "src_cod", nullable = false)
    private NewsSource source; 

    @Column(name = "map_author")
    private String mapAuthor; 
    @Column(name = "map_body")
    private String mapBody; 
    @Column(name = "map_title")
    private String mapTitle; 
    @Column(name = "map_url")
    private String mapUrl; 

}
