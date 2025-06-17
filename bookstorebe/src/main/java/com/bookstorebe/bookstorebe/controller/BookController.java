package com.bookstorebe.bookstorebe.controller;

import com.bookstorebe.bookstorebe.entity.Book;
import com.bookstorebe.bookstorebe.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Book entities.
 * Handles API requests related to books, including creation, retrieval,
 * update, and deletion.
 */
@RestController // Marks this class as a REST controller, meaning it handles RESTful requests
@RequestMapping("/api/books") // Base URL path for all endpoints in this controller
@CrossOrigin(origins = "*") // Allows requests from any origin (useful for development with separate frontend)
public class BookController {

    // Autowires the BookRepository to interact with the database for Book entities
    @Autowired
    private BookRepository bookRepository;

    /**
     * Handles HTTP POST requests to create a new book.
     * The book data is expected in the request body as JSON.
     *
     * @param book The Book object to be created, deserialized from the request body.
     * @return A ResponseEntity containing the created Book object and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        // Saves the new book to the database
        Book savedBook = bookRepository.save(book);
        // Returns the saved book with a 201 Created status
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    /**
     * Handles HTTP GET requests to retrieve all books.
     *
     * @return A ResponseEntity containing a list of all Book objects and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        // Retrieves all books from the database
        List<Book> books = bookRepository.findAll();
        // Returns the list of books with a 200 OK status
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Handles HTTP GET requests to retrieve a book by its ID.
     *
     * @param id The ID of the book to retrieve, extracted from the URL path.
     * @return A ResponseEntity containing the found Book object and HTTP status 200 (OK),
     * or 404 (Not Found) if the book does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        // Finds a book by its ID, returns Optional<Book>
        return bookRepository.findById(id)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK)) // If found, return 200 OK
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // If not found, return 404 Not Found
    }


    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        // Check if the book exists before updating
        return bookRepository.findById(id)
                .map(existingBook -> {
                    // Update existing book properties
                    existingBook.setBookName(book.getBookName());
                    existingBook.setAuthorName(book.getAuthorName());
                    existingBook.setGenre(book.getGenre());
                    existingBook.setPublishedYear(book.getPublishedYear());
                    existingBook.setQuantity(book.getQuantity());
                    Book updatedBook = bookRepository.save(existingBook);
                    return new ResponseEntity<>(updatedBook, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // If not found, return 404 Not Found
    }

    /**
     * Handles HTTP DELETE requests to delete a book by its ID.
     *
     * @param id The ID of the book to delete, extracted from the URL path.
     * @return A ResponseEntity with HTTP status 204 (No Content) if deletion is successful,
     * or 404 (Not Found) if the book does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        // Check if the book exists before deleting
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if book doesn't exist
        }
    }
}
