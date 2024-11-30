package fatec.morpheus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import fatec.morpheus.DTO.MapSourceApiDTO;
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
@Table(name = "Data_collected_api")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MapSourceApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dat_coll_api_cod")
    private int apiCollId;

    @OneToOne
    @JoinColumn(name = "api_cod", nullable = false, unique = true)
    @JsonBackReference
    private Api apiId;

    @Column(name = "dat_coll_api_registry_date", nullable = false)
    private String date;

    @Column(name = "dat_coll_api_content", nullable = false)
    private String apiContent;

    public MapSourceApi(MapSourceApiDTO mapSourceApiDTO){
        this.date = mapSourceApiDTO.getDate();
        this.apiContent = mapSourceApiDTO.getContent();
    }
}
