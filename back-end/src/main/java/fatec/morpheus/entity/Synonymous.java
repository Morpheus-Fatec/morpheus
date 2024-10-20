package fatec.morpheus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Synonymous")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(SynonymousId.class)
public class Synonymous {

    @Id
    @Column(name = "text_cod")
    private int textCod;

    @Id
    @Column(name = "syn_group")
    private int synGroup;

    @ManyToOne
    @JoinColumn(name = "text_cod", insertable = false, updatable = false)
    private Text text;

    @ManyToOne
    @JoinColumn(name = "syn_group", referencedColumnName = "text_cod", insertable = false, updatable = false)
    private Text synonym;
}
