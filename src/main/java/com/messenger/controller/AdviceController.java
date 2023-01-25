package com.messenger.controller;

import com.messenger.exp.BadRequestException;
import com.messenger.exp.ItemNotFoundException;
import com.messenger.exp.NotPermissionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler({BadRequestException.class, ItemNotFoundException.class})
    public ResponseEntity<String> handler(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<String> handler(NotPermissionException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
