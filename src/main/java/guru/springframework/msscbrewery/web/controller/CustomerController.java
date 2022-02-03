package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /*
     * (non-Javadoc) This was behaving erratically (some GUID/UUID values worked, some didn't) until I added the @PathVariable.
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("customerId") UUID customerId) {
        CustomerDto customerDto = customerService.getCustomerById(customerId);
        ResponseEntity<CustomerDto> response = new ResponseEntity<CustomerDto>(customerDto, HttpStatus.OK);
        return response;
    }

    @PostMapping
    public ResponseEntity createCustomer(@Valid @RequestBody CustomerDto customer) {
        CustomerDto newCustomer = customerService.createCustomer(customer);
        String customerLocation = getServiceUrl() + "/" + newCustomer.getId();
        System.err.println(customerLocation);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", customerLocation);

        ResponseEntity<CustomerDto> response = new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
        return response;
    }

    @PutMapping("/{customerId}")
    public ResponseEntity updateCustomer(@PathVariable("customerId") UUID customerId, @Valid @RequestBody CustomerDto customer) {
        customer.setId(customerId);
        customerService.updateCustomer(customer);
        ResponseEntity response = new ResponseEntity(HttpStatus.NO_CONTENT);
        return response;
    }

    /*
     * (non-Javadoc) This implementation show how to use void return with @ResponseStatus annotation
     */
    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomerById(customerId);
    }

    private String getServiceUrl() {
        // todo - this should resolve from properties
        String serviceHost = "http://localhost:8080";
        String serviceUri = getClass().getAnnotation(RequestMapping.class).value()[0];
        String serviceUrl = serviceHost + serviceUri;
        return serviceUrl;
    }

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
        // System.err.println("Invalid arguments found : " + ex.getMessage());

        List<String> errors = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            errors.add(fieldError.getObjectName() + "." + fieldError.getField() + ": " + fieldError.getDefaultMessage());
        });

        ResponseEntity<List> responseEntity = new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }
}
