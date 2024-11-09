package fatec.morpheus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Text")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "text_cod")
    private Integer textCode;

    @Column(name = "text_description", length = 255, unique = true)
    private String textDescription;
}