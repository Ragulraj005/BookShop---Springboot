package com.bookstorebe.bookstorebe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "books",
    uniqueConstraints = @UniqueConstraint(columnNames = {"bookName", "authorName"}),
    indexes = {
        @Index(name = "idx_book_name", columnList = "bookName"),
        @Index(name = "idx_author_name", columnList = "authorName")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_name") 
    private String authorName;
    
    @Column(name = "published_year")
    private Integer publishedYear;
    
    @Column(name = "book_name")
    private String bookName;
    
    
    @Column(name = "genre") 
    private String genre;
    
    @Column(name = "quantity")
    private Integer quantity;
}
