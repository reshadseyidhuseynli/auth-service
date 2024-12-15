//package com.example.auth_service.exception;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class Handler {
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<String> handle(ExpiredJwtException exception) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
//    }
//}
