package fatec.morpheus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Api")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_cod")
    private int code;
    @Column(name = "api_name", length = 30, unique = true)
    @Size(max = 30, message = "Name cannot exceed 30 characters")
    private String name;
    @Column(name = "api_url", length = 500, unique = true)
    @Size(max = 150, message = "API Address cannot exceed 150 characters")
    private String address;

}
