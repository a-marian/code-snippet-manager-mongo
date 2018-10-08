package io.pivotal.workshop.repository;

import io.pivotal.workshop.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
