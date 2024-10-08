package fatec.morpheus.entity;


import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private int mapId; 

    @ManyToOne 
    @JoinColumn(name = "src_cod", nullable = false)
    private NewsSource source; 

    @Column(name = "map_author")
    private String author; 
    @Column(name = "map_body")
    private String body; 
    @Column(name = "map_title")
    private String title; 
    @Column(name = "map_url")
    private String url; 
    @Column(name = "map_date")
    private Date date;

}
