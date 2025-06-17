package com.bookstorebe.bookstorebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // Import this

@SpringBootApplication
// Add the package where SecurityConfig resides
//@ComponentScan(basePackages = {"com.bookstorebe.bookstorebe", "com.bookstore.bookstorebe.config"})
public class BookstorebeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookstorebeApplication.class, args);
    }
}