package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return CustomerDto.builder().id(customerId)
                .name("Sam Smith")
                .build();
    }

    @Override
    public CustomerDto createCustomer(@Valid CustomerDto customer) {
        UUID customerId = UUID.randomUUID();
        String customerName = customer.getName();
        return CustomerDto.builder().id(customerId)
                .name(customerName)
                .build();
    }

    @Override
    public void updateCustomer(CustomerDto customer) {
        // todo - provide actual implementation later
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        // todo - provide actual implementation later
    }
}

