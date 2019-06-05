package il.co.rudakov.pollingservice.error_hadler;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;

@ControllerAdvice
public class GlobalExceptionHandler /*implements AsyncUncaughtExceptionHandler*/ {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<String> Status400Handler(Exception400 ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception409.class)
    public ResponseEntity<String> Status409Handler(Exception409 ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }


/*    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        System.out.println("error handler works!!");
        throw new Exception400("Party not exists. Check the list of parties first!");
    }*/
}
