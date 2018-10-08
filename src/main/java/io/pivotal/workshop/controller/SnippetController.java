package io.pivotal.workshop.controller;

import java.util.List;

import io.pivotal.workshop.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.pivotal.workshop.domain.Snippet;
import io.pivotal.workshop.repository.SnippetRepository;


@RestController
public class SnippetController {

	private static final Logger log = LoggerFactory.getLogger(SnippetController.class);
	 @Autowired
	    SnippetRepository snippetRepository;

	    @GetMapping("/snippets")
	    public List<Snippet> snippets() {
	        return (List<Snippet>) snippetRepository.findAll();
	    }

	    @RequestMapping("/snippets/{id}")
	    public ResponseDto snippet(@PathVariable("id") String id) {
	        ResponseDto response = new ResponseDto();
	        Snippet snippet = snippetRepository.findOne(id);
			response.setStatus(HttpStatus.OK);
	    	return response;

	    }

	    @PostMapping("/snippets")
	    public ResponseEntity<?> add(@RequestBody Snippet snippet) {
	        Snippet _snippet = snippetRepository.save(snippet);
	        assert _snippet != null;
			log.info("get snippets");
	        HttpHeaders httpHeaders = new HttpHeaders();
	        httpHeaders.setLocation(ServletUriComponentsBuilder
	                .fromCurrentRequest().path("/" + _snippet.getId())
	                .buildAndExpand().toUri());

	        return new ResponseEntity<>(_snippet, httpHeaders, HttpStatus.CREATED);
	    }
}
