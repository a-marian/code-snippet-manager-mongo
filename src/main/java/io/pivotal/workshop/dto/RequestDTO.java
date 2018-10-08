package io.pivotal.workshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"header", "body"})
public class RequestDTO {

    @JsonProperty(value = "header")
     RequestHeader header;

    @JsonProperty(value = "body")
     RequestBody body;


    public RequestHeader getHeader() {
        return header;
    }

    public void setHeader(RequestHeader header) {
        this.header = header;
    }

    public RequestBody getBody() {
        return body;
    }

    public void setBody(RequestBody body) {
        this.body = body;
    }
}
