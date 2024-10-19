package fatec.morpheus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "News_author")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_aut_id")
    private int autId;

    @Column(name = "new_aut_name", length = 30, unique = true)
    @Size(max = 30, message = "Name cannot exceed 30 characters")
    private String autName;

}
