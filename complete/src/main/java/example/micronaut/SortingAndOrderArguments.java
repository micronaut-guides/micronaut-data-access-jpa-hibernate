package example.micronaut;

import io.micronaut.core.annotation.Introspected;

import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Optional;

@Introspected
public class SortingAndOrderArguments {

    @Nullable
    @PositiveOrZero // <1>
    private Integer offset;

    @Nullable
    @Positive // <1>
    private Integer max;

    @Nullable
    @Pattern(regexp = "id|name")  // <1>
    private String sort;

    @Pattern(regexp = "asc|ASC|desc|DESC")  // <1>
    @Nullable
    private String order;

    public SortingAndOrderArguments() {

    }

    public Optional<Integer> getOffset() {
        if (offset == null) {
            return Optional.empty();
        }
        return Optional.of(offset);
    }

    public void setOffset(@Nullable Integer offset) {
        this.offset = offset;
    }

    public Optional<Integer> getMax() {
        if (max == null) {
            return Optional.empty();
        }
        return Optional.of(max);
    }

    public void setMax(@Nullable Integer max) {
        this.max = max;
    }

    public Optional<String> getSort() {
        if (sort == null) {
            return Optional.empty();
        }
        return Optional.of(sort);
    }

    public void setSort(@Nullable String sort) {
        this.sort = sort;
    }

    public Optional<String> getOrder() {
        if (order == null) {
            return Optional.empty();
        }
        return Optional.of(order);
    }

    public void setOrder(@Nullable String order) {
        this.order = order;
    }
}
