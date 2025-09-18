package com.eventmanagement.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.eventmanagement.dto.ResponseStructure;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleIdNotFoundException(IdNotFoundException exception) {
        ResponseStructure<String> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.NOT_FOUND.value());
        structure.setMessage("Failure");
        structure.setData(exception.getMessage());

        return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidSortField(InvalidSortFieldException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoRecordFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleNoRecordFoundException(NoRecordFoundException exception) {
        ResponseStructure<String> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.NOT_FOUND.value());
        structure.setMessage("Failure");
        structure.setData(exception.getMessage());

        return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseStructure<String>> handleInvalidInput(InvalidInputException ex) {
        ResponseStructure<String> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.BAD_REQUEST.value());
        structure.setMessage("Invalid Input");
        structure.setData(ex.getMessage());

        return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
    }

   
}
