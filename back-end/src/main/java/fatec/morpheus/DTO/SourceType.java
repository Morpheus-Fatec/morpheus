package fatec.morpheus.DTO;

public enum SourceType {
    SITE(1),
    API(2);

    private final int displayName;

    SourceType(int displayName) {
        this.displayName = displayName;
    }

    public int getDisplayName() {
        return displayName;
    }

}
