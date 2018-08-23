package io.pivotal.workshop.repository;


import org.springframework.data.repository.CrudRepository;
import io.pivotal.workshop.domain.Snippet;


public interface SnippetRepository extends CrudRepository
<Snippet, String>{

}
