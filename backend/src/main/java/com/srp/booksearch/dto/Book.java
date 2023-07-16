package com.srp.booksearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String id;
    private String title;
    private Author []authors;
    private Integer coverImage;
}
