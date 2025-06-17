package com.bookstorebe.bookstorebe.service;

import java.util.List;

import com.bookstorebe.bookstorebe.entity.Book;

public interface BookService {
  
	Book createBook(Book book);
	Book updateBook(Long id , Book book);
	void deleteBook(Long id);
	Book getBookById(Long id);
	List<Book> getAllBooks();
}
