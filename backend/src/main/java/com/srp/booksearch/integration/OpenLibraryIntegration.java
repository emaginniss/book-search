package com.srp.booksearch.integration;

import com.srp.booksearch.dto.Author;
import com.srp.booksearch.dto.Book;
import com.srp.booksearch.model.SearchResults;
import feign.Feign;
import feign.Logger;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OpenLibraryIntegration {

    private final BookSearchClient bookClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(OpenLibraryIntegration.class))
            .logLevel(Logger.Level.FULL)
            .target(BookSearchClient.class, "https://openlibrary.org");

    public SearchResults search(String searchTerm) {
        OLResponse resp = bookClient.search(searchTerm);

        return new SearchResults(searchTerm,
                Arrays.stream(resp.docs).map(this::newBook).toList(),
                System.currentTimeMillis());
    }

    private Book newBook(OLBook r) {
        List<Author> authors = new ArrayList<>();
        if (r.author_key != null && r.author_name != null) {
            for (int i = 0; i < r.author_name.length && i < r.author_key.length; i++) {
                authors.add(new Author(r.author_name[i], r.author_key[i]));
            }
        }
        return new Book(r.key, r.title, authors.toArray(new Author[0]), r.cover_i);
    }

    public interface BookSearchClient {
        @RequestLine("GET /search.json?q={q}")
        OLResponse search(@Param("q") String q);
    }

    public static class OLResponse {
        OLBook[] docs;
    }

    public static class OLBook {
        Integer cover_i;
        String title;
        String[] author_name;
        String key;
        String[] author_key;
    }
}
