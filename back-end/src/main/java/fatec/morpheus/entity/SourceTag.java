package fatec.morpheus.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

    @Column(name = "src_tag_name", length = 50)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<NewsSource> sources = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    private Set<News> news = new HashSet<>();
}
