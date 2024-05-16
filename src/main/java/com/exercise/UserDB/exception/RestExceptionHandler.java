package com.exercise.UserDB.exception;

import graphql.GraphQLError;
import graphql.GraphQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<Object> handleException(NoUserFoundException ex) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GraphQLException.class)
    public GraphQLError handle(GraphQLError graphQLError) {
        return GraphQLError.newError().message(graphQLError.getMessage()).build();
    }
}
