package fatec.morpheus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Texto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Texto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "texto_cod")
    private Integer textoCod;

    @Column(name = "texto_description", length = 255, unique = true)
    private String textoDescription;

    @ManyToMany
    @JoinTable(
        name = "Synonymous",
        joinColumns = @JoinColumn(name = "texto_cod"),
        inverseJoinColumns = @JoinColumn(name = "syn_group")
    )
    private List<Texto> synonyms;
}