package io.pivotal.workshop.repository;

import io.pivotal.workshop.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, String> {
}
