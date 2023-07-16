package com.srp.booksearch.model;

import com.srp.booksearch.dto.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchResults {

    private String searchTerm;
    private List<Book> books;
    private long retrievalTime;

}
