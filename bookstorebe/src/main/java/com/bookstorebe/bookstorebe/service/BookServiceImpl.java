package com.bookstorebe.bookstorebe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstorebe.bookstorebe.entity.Book;
import com.bookstorebe.bookstorebe.repository.BookRepository;
import com.bookstorebe.bookstorebe.exception.DuplicateBookException;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public Book createBook(Book book) {
	    Optional<Book> existingBook = bookRepository.findByBookNameAndAuthorName(
	        book.getBookName(), book.getAuthorName());

	    if (existingBook.isPresent()) {
	        throw new DuplicateBookException("Book already exists.");
	    }

	    return bookRepository.save(book);
	}
	@Override
	public Book updateBook(Long id , Book bookDetails) {
		Book book  = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
		book.setAuthorName(bookDetails.getAuthorName());
		book.setBookName(bookDetails.getBookName());
		book.setGenre(bookDetails.getGenre());
		book.setPublishedYear(bookDetails.getPublishedYear());
		book.setQuantity(bookDetails.getQuantity());
		return bookRepository.save(book);
	}
    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
	
}
