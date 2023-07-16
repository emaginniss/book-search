package com.srp.booksearch.api;

import com.srp.booksearch.dto.BookSearchResponse;
import com.srp.booksearch.manager.BookSearchManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookSearchControllerTest {

    BookSearchManager bsm = mock(BookSearchManager.class);
    BookSearchController bsc = new BookSearchController(bsm);

    @Test
    public void testGoodInput() {
        when(bsm.search(anyString())).thenReturn(new BookSearchResponse("x", new ArrayList<>(), 0L));
        BookSearchResponse out = bsc.search("x");
        assertNotNull(out);
    }

    @Test
    public void testNullInput() {
        assertThrows(RuntimeException.class, () -> bsc.search(null));
        assertThrows(RuntimeException.class, () -> bsc.search(" "));
    }

    @Test
    public void testLongInput() {
        String search = StringUtils.repeat("1234567890", 30);
        assertThrows(RuntimeException.class, () -> bsc.search(search));
    }
}
