package io.pivotal.workshop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.pivotal.workshop.domain.Snippet;
import io.pivotal.workshop.repository.SnippetRepository;


@RestController
public class SnippetController {

	 @Autowired
	    SnippetRepository snippetRepository;

	    @GetMapping("/snippets")
	    public List<Snippet> snippets() {
	        return (List<Snippet>) snippetRepository.findAll();
	    }

	    @GetMapping("/snippets/{id}")
	    public Optional<Snippet> snippet(@PathVariable("id") String id) {
	        return snippetRepository.findById(id);
	    }

	    @PostMapping("/snippets")
	    public ResponseEntity<?> add(@RequestBody Snippet snippet) {
	        Snippet _snippet = snippetRepository.save(snippet);
	        assert _snippet != null;

	        HttpHeaders httpHeaders = new HttpHeaders();
	        httpHeaders.setLocation(ServletUriComponentsBuilder
	                .fromCurrentRequest().path("/" + _snippet.getId())
	                .buildAndExpand().toUri());

	        return new ResponseEntity<>(_snippet, httpHeaders, HttpStatus.CREATED);
	    }
}
