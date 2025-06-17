package com.bookstorebe.bookstorebe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstorebe.bookstorebe.entity.Book;
     @Repository
	public interface BookRepository extends JpaRepository<Book, Long> {


		Optional<Book> findByBookNameAndAuthorName(String bookName, String authorName);
		
	
	}
