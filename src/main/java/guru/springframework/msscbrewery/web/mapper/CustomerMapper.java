package guru.springframework.msscbrewery.web.mapper;

import guru.springframework.msscbrewery.domain.Customer;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {
    public CustomerDto asCustomerDto(final Customer customer);
    public Customer asCustomer(final CustomerDto customerDto);
}
