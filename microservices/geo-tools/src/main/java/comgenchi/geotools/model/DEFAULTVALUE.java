package comgenchi.geotools.model;

public enum DEFAULTVALUE {
    PAGE(1), SIZE(50);
    private int value;

    DEFAULTVALUE(int value) {
        this.value=value;
    }

    public int get() {
        return value;
    }

}
