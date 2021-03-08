package com.bortoti.accountmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

public abstract class AbstractRestController<T> {

  protected URI getCreatedURI(T id) {
    return getCurrentRequestUriBuilder().path("/{id}").buildAndExpand(id).toUri();
  }

  protected URI getCreatedURI() {
    return getCurrentRequestUriBuilder().build().toUri();
  }

  protected ResponseEntity<String> newCreatedResponse(T id) {
    String body = id instanceof Long ? "{ \"id\": " + id + "}" : "{ \"id\": \"" + id + "\"}";
    return newCreatedResponse(id, body);
  }

  protected <B> ResponseEntity<B> newCreatedResponse(T id, B body) {
    URI location = getCreatedURI(id);
    return created(location).body(body);
  }

  protected <B> ResponseEntity<B> newCreatedResponseWithoutID(B body) {
    URI location = getCreatedURI();
    return created(location).body(body);
  }

  protected ServletUriComponentsBuilder getCurrentRequestUriBuilder() {
    return fromCurrentRequest();
  }
}