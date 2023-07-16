package com.srp.booksearch.manager;

import com.srp.booksearch.dto.BookSearchResponse;
import com.srp.booksearch.integration.OpenLibraryIntegration;
import com.srp.booksearch.model.SearchResults;
import com.srp.booksearch.repo.BookSearchRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BookSearchManager {

    private final BookSearchRepo bookSearchRepo;
    private final OpenLibraryIntegration openLibraryIntegration;

    public BookSearchManager(BookSearchRepo bookSearchRepo, OpenLibraryIntegration openLibraryIntegration) {
        this.bookSearchRepo = bookSearchRepo;
        this.openLibraryIntegration = openLibraryIntegration;
    }

    /**
     * Load the search results from the cache if possible or pull from OpenLibrary
     * @param searchTerm The term to search for.
     * @return The search results retrieved from the cache or from OpenLibrary
     */
    public BookSearchResponse search(String searchTerm) {
        log.info("Executing search for term " + searchTerm);
        // Check the cache for the search results
        Optional<SearchResults> results = bookSearchRepo.getValidCache(searchTerm);
        if (results.isEmpty()) {
            // Results were not found.  Pull the data from OpenLibrary and then store in the cache
            log.info("Cache Miss - Connecting to OpenLibrary for term " + searchTerm);
            results = Optional.of(openLibraryIntegration.search(searchTerm));
            bookSearchRepo.cache(results.get());
        } else {
            log.info("Cache Hit - Cached data was found for " + searchTerm);
        }
        // Convert the search results into a response that can be returned to the user
        return results.map(r -> new BookSearchResponse(r.getSearchTerm(), r.getBooks(), r.getRetrievalTime())).get();
    }

}
