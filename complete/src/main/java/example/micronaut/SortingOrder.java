package example.micronaut;

public enum SortingOrder {
    ASC, DESC;

    public final static SortingOrder DEFAULT_SORTING_ORDER = SortingOrder.ASC;

    public static SortingOrder of(String str) {
        if (str != null) {
            if (ASC.toString().equalsIgnoreCase(str)) {
                return ASC;
            }
            if (DESC.toString().equalsIgnoreCase(str)) {
                return DESC;
            }
        }
        return DEFAULT_SORTING_ORDER;
    }
}
