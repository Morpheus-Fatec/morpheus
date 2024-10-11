package fatec.morpheus.entity;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.annotation.Generated;
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
@Table(name = "News")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_cod")
    private int newsCod;

    @Column(name = "new_title", length = 70)
    @Size(max = 70, message = "News Title cannot exceed 70 characters")
    private String newsTitle;

    @Column(name = "new_content")
    private String newsContent;

    @Column(name = "new_registry_date", updatable = false)
    @CreationTimestamp
    private Date newsRegistryDate;

}
