package fatec.morpheus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Synonymous")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(SynonymousId.class)
public class Synonymous {

    @Id
    @Column(name = "texto_cod")
    private int textoCod;

    @Id
    @Column(name = "syn_group")
    private int synGroup;

    @ManyToOne
    @JoinColumn(name = "texto_cod", insertable = false, updatable = false)
    private Texto texto;

    @ManyToOne
    @JoinColumn(name = "syn_group", referencedColumnName = "texto_cod", insertable = false, updatable = false)
    private Texto synonym;
}
