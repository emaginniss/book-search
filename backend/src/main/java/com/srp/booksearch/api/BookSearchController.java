package com.srp.booksearch.api;

import com.srp.booksearch.dto.BookSearchResponse;
import com.srp.booksearch.manager.BookSearchManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BookSearchController {

    private final BookSearchManager bookSearchManager;

    public BookSearchController(BookSearchManager bookSearchManager) {
        this.bookSearchManager = bookSearchManager;
    }

    @GetMapping("/api/search")
    public BookSearchResponse search(@RequestParam("q") String searchTerm) {
        searchTerm = StringUtils.trimToNull(searchTerm);
        if (searchTerm == null || searchTerm.length() > 200) {
            throw new RuntimeException("Invalid search term");
        }
        return bookSearchManager.search(searchTerm);
    }

}
