package fatec.morpheus.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
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
    @Size(max = 30, message = "Name cannot exceed 30 characters")
    private String srcName;

    @Column(name = "src_type")
    private String type;

    @Column(name = "src_address", length = 150, unique = true)
    @Size(max = 150, message = "Source Address cannot exceed 150 characters")
    private String address;

    @Column(name = "src_registry_date", updatable = false)
    @CreationTimestamp
    private Date registrationDate;

    @OneToOne(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private MapSource map;

    @Transient
    public List<Integer> getTagCodes() {
        return tagCodes;
    }

    @ElementCollection
    @CollectionTable(name = "Source_tag", joinColumns = @JoinColumn(name = "src_cod"))
    @Column(name = "tag_cod")
    private List<Integer> tagCodes;

    public void setTagCodes(List<Integer> tagCodes) {
        if (tagCodes != null) {
            this.tagCodes = new ArrayList<>(tagCodes);
        } else {
            this.tagCodes = new ArrayList<>();
        }
    }
}

