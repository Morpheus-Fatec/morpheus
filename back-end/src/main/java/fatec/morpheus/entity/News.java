package fatec.morpheus.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "new_aut_cod", referencedColumnName = "new_aut_id")
    private NewsAuthor newsAuthor;

    @ManyToOne
    @JoinColumn(name = "new_src_cod", referencedColumnName = "src_cod")
    private NewsSource sourceNews;

    @ManyToMany
    @JoinTable(
        name = "News_tag",
        joinColumns = @JoinColumn(name = "new_cod"),
        inverseJoinColumns = @JoinColumn(name = "src_tag_cod")
    )
    private Set<SourceTag> tags = new HashSet<>();
}