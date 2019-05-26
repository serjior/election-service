package il.co.rudakov.pollingservice.error_hadler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<String> Status400Handler(Exception400 ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception409.class)
    public ResponseEntity<String> Status409Handler(Exception409 ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }


}
