import fatec.morpheus.entity.News;
import fatec.morpheus.entity.SourceTag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "news_tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsTag {

    @Id
    @Column(name = "new_cod")
    private int newCod;

    @Id
    @Column(name = "src_tag_cod")
    private int srcTagCod;

    @ManyToOne
    @JoinColumn(name = "new_cod", referencedColumnName = "new_cod", insertable = false, updatable = false)
    private News news;

    @ManyToOne
    @JoinColumn(name = "src_tag_cod", referencedColumnName = "src_tag_cod", insertable = false, updatable = false)
    private SourceTag sourceTag;

    @Override
    public String toString() {
        return "NewsTag{" +
                "newCod=" + newCod +
                ", srcTagCod=" + srcTagCod +
                ", news=" + news +
                ", sourceTag=" + sourceTag +
                '}';
    }
}
