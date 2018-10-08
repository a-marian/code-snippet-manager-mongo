package io.pivotal.workshop.controller;


import io.pivotal.workshop.domain.User;
import io.pivotal.workshop.dto.ResponseDto;
import io.pivotal.workshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public List<User> users() {
        return (List<User>) userRepository.findAll();
    }

    @RequestMapping("/users/{id}")
    public ResponseDto findUserById(@PathVariable("id") String id) {
        ResponseDto response = new ResponseDto();
        User user = userRepository.findOne(id);
        response.setUser(user);
        response.setStatus(HttpStatus.OK);
        return response;

    }

    @PostMapping("/users")
    public ResponseEntity<?> add(@RequestBody User user) {
        User userToSave = userRepository.save(user);
        assert userToSave != null;
        log.info(" saving new user {}", userToSave);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + userToSave.getId())
                .buildAndExpand().toUri());

        return new ResponseEntity<>(userToSave, httpHeaders, HttpStatus.CREATED);
    }
}
