package io.pivotal.workshop.controller;

import io.pivotal.workshop.domain.Book;

import io.pivotal.workshop.dto.RequestDTO;
import io.pivotal.workshop.dto.ResponseDto;
import io.pivotal.workshop.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class BookController {

    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookRepository bookRepository;


    @RequestMapping("/books/{id}")
    public ResponseDto getFavoriteFilm(@PathVariable("id") String id) {
        ResponseDto response = new ResponseDto();

        Book book = bookRepository.findOne(id);
        response.setStatus(HttpStatus.OK);
        return response;

    }

    @PostMapping("/books")
    public ResponseEntity<?> add(@RequestBody RequestDTO request) {

        Book bookToSave = new Book();
        assert request != null;
            bookToSave.setId(request.getBody().getId());
            bookToSave.setName(request.getBody().getName());
            bookToSave.setSubject(request.getBody().getCategory());
            bookRepository.save(bookToSave);

        LOG.info("Saving Film {}", bookToSave);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + bookToSave.getId())
                .buildAndExpand().toUri());

        return new ResponseEntity<>(request.getBody(), httpHeaders, HttpStatus.CREATED);
    }


}
