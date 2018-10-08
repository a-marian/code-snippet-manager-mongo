package io.pivotal.workshop.controller;

import io.pivotal.workshop.domain.Film;
import io.pivotal.workshop.dto.FinalResponseDTO;
import io.pivotal.workshop.dto.RequestDTO;
import io.pivotal.workshop.dto.ResponseDto;
import io.pivotal.workshop.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FilmController {

    private static final Logger LOG = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    private FilmRepository filmRepository;

    @RequestMapping("/films/{id}")
    public ResponseDto getFavoriteFilm(@PathVariable("id") String id) {
        ResponseDto response = new ResponseDto();
        Film film = filmRepository.findOne(id);
        response.setStatus(HttpStatus.OK);
        return response;

    }

    @PostMapping("/films")
    public ResponseEntity<?> add(@RequestBody RequestDTO request) {
        Film filmToSave = new Film();
        FinalResponseDTO finalResponseDTO = new FinalResponseDTO();

        assert request != null;
        filmToSave.setId(request.getBody().getId());
        filmToSave.setName(request.getBody().getName());
        filmToSave.setGenre(request.getBody().getCategory());
        filmRepository.save(filmToSave);
        LOG.info("Saving Film ");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + filmToSave.getId())
                .buildAndExpand().toUri());

        return new ResponseEntity<>(request.getBody(), httpHeaders, HttpStatus.CREATED);
    }



}
