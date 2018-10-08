package io.pivotal.workshop.repository;

import io.pivotal.workshop.domain.Film;
import org.springframework.data.repository.CrudRepository;

public interface FilmRepository extends CrudRepository<Film, String> {
}
