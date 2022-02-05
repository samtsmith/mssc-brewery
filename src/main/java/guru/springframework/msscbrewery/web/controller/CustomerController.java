package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

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

}
