package com.srp.booksearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchResponse {

    private String searchTerm;
    private List<Book> books;
    private long retrievalTime;

}
