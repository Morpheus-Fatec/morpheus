package fatec.morpheus.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
public class ApiContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dat_coll_api_cod")
    private int apiCollId;

    @ManyToOne
    @JoinColumn(name = "api_cod", nullable = false)
    @JsonBackReference
    private Api apiId;

    @Column(name = "dat_coll_api_registry_date", nullable = false)
    private String date;

    @Lob
    @Column(name = "dat_coll_api_content", nullable = false, columnDefinition = "LONGTEXT")
    private String apiContent;

    @Column(name = "da_coll_api_address", nullable = false)
    private String apiAddress;

    @Column(name = "da_coll_api_method", nullable = false, length = 10)
    private String method;
}

