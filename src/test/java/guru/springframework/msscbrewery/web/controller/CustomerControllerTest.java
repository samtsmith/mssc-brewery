package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    private final String serviceUri = "/api/v1/customer/";

    @Test
    public void getCustomer() throws Exception {
        mockMvc.perform(get(serviceUri + UUID.randomUUID()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createCustomer() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                //.id(UUID.randomUUID())
                .name("Sam Smith")
                .build();
        String json = objectMapper.writeValueAsString(customerDto);

        Mockito.when(customerService.createCustomer(customerDto)).thenReturn(CustomerDto.builder().id(UUID.randomUUID()).build());

        mockMvc.perform(post(serviceUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateCustomer() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                .name("Sam Smith")
                .build();
        String json = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(put(serviceUri + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCustomer() throws Exception {
        mockMvc.perform(delete(serviceUri + UUID.randomUUID()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
