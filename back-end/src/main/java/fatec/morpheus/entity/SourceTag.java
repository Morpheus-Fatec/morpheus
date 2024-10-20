package fatec.morpheus.entity;

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

@Entity
@Table(name = "Source_tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SourceTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "src_tag_cod")
    private int id;

    @ManyToOne
    @JoinColumn(name = "src_cod", referencedColumnName = "src_cod")
    private NewsSource source;

    @ManyToOne
    @JoinColumn(name = "tag_cod", referencedColumnName = "tag_cod")
    private Tag tag;
}