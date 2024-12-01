package fatec.morpheus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Data_collected_api")
public class DataCollectedApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dat_coll_api_cod")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_cod", nullable = false)
    private Api api;

    @Column(name = "dat_coll_api_registry_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registryDate;

    @Lob    
    @Column(name = "dat_coll_api_content", columnDefinition = "LONGTEXT")
    private String content;
}