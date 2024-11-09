package fatec.morpheus.entity;

import java.io.Serializable;
import java.util.Objects;

public class SynonymousId implements Serializable {
    private Integer textCode;
    private Integer synGroup;

    public SynonymousId() {}

    public SynonymousId(Integer textCode, Integer synGroup) {
        this.textCode = textCode;
        this.synGroup = synGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SynonymousId)) return false;
        SynonymousId that = (SynonymousId) o;
        return Objects.equals(textCode, that.textCode) && Objects.equals(synGroup, that.synGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textCode, synGroup);
    }
}
