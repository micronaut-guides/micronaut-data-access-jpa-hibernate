package example.micronaut;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortingOrderTest {

    @Test
    public void testSortingOrderOfMethod() {
        assertEquals(SortingOrder.ASC,SortingOrder.of("asc"));
        assertEquals(SortingOrder.ASC,SortingOrder.of("ASC"));
        assertEquals(SortingOrder.DESC,SortingOrder.of("desc"));
        assertEquals(SortingOrder.DESC,SortingOrder.of("DESC"));
        assertEquals(SortingOrder.DEFAULT_SORTING_ORDER,SortingOrder.of("foo"));
        assertEquals(SortingOrder.DEFAULT_SORTING_ORDER,SortingOrder.of(null));

    }
}
