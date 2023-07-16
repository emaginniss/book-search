create table book_search_cache
(
    search_term varchar(200) primary key,
    results     text,
    cache_time  bigint
);
