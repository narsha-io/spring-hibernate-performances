package io.narsha.spring.jpa.troubleshooting.repository;

import io.narsha.spring.jpa.troubleshooting.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
