package com.srp.booksearch.repo;

import com.google.gson.Gson;
import com.srp.booksearch.model.SearchResults;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class BookSearchRepo {

    private static final long MINUTE = 60 * 1000;

    private static final Gson gson = new Gson();

    private final EntityManager entityManager;
    private final long cacheTimeout;

    public BookSearchRepo(EntityManager entityManager, @Value("${cache.timout:10}") long cacheTimeoutInMinutes) {
        this.entityManager = entityManager;
        cacheTimeout = cacheTimeoutInMinutes * MINUTE;
    }

    /**
     * Check the cache for results corresponding to the search term that were stored within the cache invalidation
     * period.
     * @param searchTerm The term to search for
     * @return Search results if found in the cache and valid
     */
    public Optional<SearchResults> getValidCache(String searchTerm) {
        return entityManager.createNativeQuery("select results from book_search_cache where search_term = ? and cache_time >= ?")
                .setParameter(1, searchTerm.toLowerCase())
                .setParameter(2, cacheInvalidationTime())
                .getResultList()
                .stream()
                .findFirst()
                .map(row -> gson.fromJson((String) row, SearchResults.class));
    }

    /**
     * Store the search results in the cache table.
     * This method uses a more complex sql command that acts as an upsert.  It attempts an insert and if it finds that
     * the insert fails because of a conflict on the primary key of searchTerm, it instead executes an update.  This
     * method reduces the round trips against the database.
     * @param results The search results to store
     */
    @Transactional
    public void cache(SearchResults results) {
        String resultsJson = gson.toJson(results);
        entityManager.createNativeQuery(
                "insert into book_search_cache (search_term, results, cache_time) values (?, ?, ?) " +
                        "on conflict(search_term) do update set results = ?, cache_time = ?")
                .setParameter(1, results.getSearchTerm().toLowerCase())
                .setParameter(2, resultsJson)
                .setParameter(3, results.getRetrievalTime())
                .setParameter(4, resultsJson)
                .setParameter(5, results.getRetrievalTime())
                .executeUpdate();
    }

    private long cacheInvalidationTime() {
        return System.currentTimeMillis() - cacheTimeout;
    }
}
