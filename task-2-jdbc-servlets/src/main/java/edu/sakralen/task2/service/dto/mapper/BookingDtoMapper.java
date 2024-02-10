package edu.sakralen.task2.service.dto.mapper;

import edu.sakralen.task2.model.SessionCustomer;
import edu.sakralen.task2.service.dto.BookingDto;
import org.mapstruct.Mapper;

@Mapper
public interface BookingDtoMapper extends DtoMapper<SessionCustomer, BookingDto> {
}
