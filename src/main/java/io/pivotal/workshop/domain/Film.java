package io.pivotal.workshop.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class Film {

    @Id
    private String id;
    private String name;
    private String genre;


    public Film (){}

    public Film(String name, String genre){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
