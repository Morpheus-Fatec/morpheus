package fatec.morpheus.entity;

import java.io.Serializable;
import java.util.Objects;

public class SynonymousId implements Serializable {
    private Integer textCod;
    private Integer synGroup;

    public SynonymousId() {}

    public SynonymousId(Integer textCod, Integer synGroup) {
        this.textCod = textCod;
        this.synGroup = synGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SynonymousId)) return false;
        SynonymousId that = (SynonymousId) o;
        return Objects.equals(textCod, that.textCod) && Objects.equals(synGroup, that.synGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textCod, synGroup);
    }
}
