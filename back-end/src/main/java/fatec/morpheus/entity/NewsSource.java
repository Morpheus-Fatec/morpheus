package fatec.morpheus.entity;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Source")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "src_cod")
    private int code;

    @Column(name = "src_name", length = 30, unique = true)
    private String srcName;

    @Column(name = "src_type")
    private String type;

    @Column(name = "src_address", length = 100 ,unique = true)
    private String address;

    @Column(name = "src_registry_date", updatable = false)
    @CreationTimestamp
    private Date registrationDate;

    @ManyToMany
    @JoinTable(
        name = "source_tag",
        joinColumns = @JoinColumn(name = "src_cod"),
        inverseJoinColumns = @JoinColumn(name = "tag_cod")
    )
    private List<Tag> tags;
}
