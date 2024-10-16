package fatec.morpheus.entity;

import jakarta.persistence.*;
import lombok.*;

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
}