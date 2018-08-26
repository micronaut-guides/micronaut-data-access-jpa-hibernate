package example.micronaut;

public class SortingArguments {
    private String sort;
    private SortingOrder order;

    public SortingArguments(String sort, SortingOrder order) {
        this.sort = sort;
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public SortingOrder getOrder() {
        return order;
    }
}
