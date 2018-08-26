package example.micronaut;

public class PaginationArguments {
    Integer offset;
    Integer max;

    public PaginationArguments(Integer offset, Integer max) {
        this.offset = offset == null || offset < 0 ? 0 : offset;
        this.max = max;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getMax() {
        return max;
    }

}
