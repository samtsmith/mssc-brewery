package guru.springframework.msscbrewery.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class MvcExceptionHandler {
    /**
     * if you do not validate using @Valid annotation, and exception is raised by hibernate at jpa layer it generates
     * ConstraintViolationException, this exception is part of Javax bean validation framework, and raised at the time
     * of performing persistence operation (before the actual sql execution)
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = new ArrayList();
        ex.getConstraintViolations().forEach(constraintViolation -> {
            errors.add(constraintViolation.getRootBeanClass().getName() + " " + constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
        });

        ResponseEntity<List> responseEntity = new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    /**
     * If validation happens at controller/service layer by using @Valid annotation, it generates
     * MethodArgumentNotValidException, you can add handler for this and return the response accordingly, this class
     * is part of spring framework and validation is performed by spring framework
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        // Uncomment to see the full details of what is captured
        //System.err.println("Invalid arguments found : " + ex.getMessage());

        List<String> errors = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            errors.add(fieldError.getObjectName() + "." + fieldError.getField() + ": " + fieldError.getDefaultMessage());
        });

        ResponseEntity<List> responseEntity = new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }
}
