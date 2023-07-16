package com.srp.booksearch.manager;

import com.srp.booksearch.dto.BookSearchResponse;
import com.srp.booksearch.integration.OpenLibraryIntegration;
import com.srp.booksearch.model.SearchResults;
import com.srp.booksearch.repo.BookSearchRepo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookSearchManagerTest {

    BookSearchRepo bsr = mock(BookSearchRepo.class);
    OpenLibraryIntegration oli = mock(OpenLibraryIntegration.class);
    BookSearchManager bsm = new BookSearchManager(bsr, oli);

    @Test
    public void testFound() {
        when(bsr.getValidCache("x")).thenReturn(Optional.of(new SearchResults("x", new ArrayList<>(), 0L)));
        ArgumentCaptor<SearchResults> resultCaptor = ArgumentCaptor.forClass(SearchResults.class);
        doNothing().when(bsr).cache(resultCaptor.capture());
        BookSearchResponse resp = bsm.search("x");
        assertNotNull(resp);
        assertEquals(0, resultCaptor.getAllValues().size());
    }

    @Test
    public void testNotFound() {
        when(bsr.getValidCache("x")).thenReturn(Optional.empty());
        when(oli.search("x")).thenReturn(new SearchResults("x", new ArrayList<>(), 0L));
        ArgumentCaptor<SearchResults> resultCaptor = ArgumentCaptor.forClass(SearchResults.class);
        doNothing().when(bsr).cache(resultCaptor.capture());
        BookSearchResponse resp = bsm.search("x");
        assertNotNull(resp);
        assertNotNull(resultCaptor.getValue());
    }
}
