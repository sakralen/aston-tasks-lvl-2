package edu.sakralen.task2.service.dto.mapper;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.service.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerDtoMapper extends DtoMapper<Customer, CustomerDto> {
}
