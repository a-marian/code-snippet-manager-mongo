package io.pivotal.workshop.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class Book {

    private String id;
    private String name;
    private String subject;

    public Book(){}

    public Book(String name, String subject){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.subject = subject;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
