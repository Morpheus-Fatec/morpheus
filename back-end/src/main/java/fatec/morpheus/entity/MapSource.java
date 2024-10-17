package fatec.morpheus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import fatec.morpheus.DTO.MapSourceDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Map_source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MapSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private int mapId;

    @OneToOne 
    @JoinColumn(name = "src_cod", nullable = false, unique = true)
    @JsonBackReference
    private NewsSource source; 

    @Column(name = "map_author", nullable = false)
    private String author; 

    @Column(name = "map_body", nullable = false)
    private String body; 

    @Column(name = "map_title", nullable = false)
    private String title; 

    @Column(name = "map_date", nullable = false)
    private String date;

    public MapSource(MapSourceDTO mapSourceDTO) {
        this.author = mapSourceDTO.getAuthor();
        this.body = mapSourceDTO.getBody();
        this.title = mapSourceDTO.getTitle();
        this.date = mapSourceDTO.getDate();
    }
}
