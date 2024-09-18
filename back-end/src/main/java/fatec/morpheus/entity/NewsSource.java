package fatec.morpheus.entity;

import java.sql.Date;

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

    @Column(name = "src_name")
    private String srcName;

    @Column(name = "src_type")
    private String type;

    @Column(name = "src_address")
    private String address;

    @Column(name = "src_registry_date", insertable = false)
    private Date registrationDate;

}
