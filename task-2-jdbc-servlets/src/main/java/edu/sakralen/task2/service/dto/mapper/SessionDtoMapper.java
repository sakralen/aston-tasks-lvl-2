package edu.sakralen.task2.service.dto.mapper;

import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.service.dto.SessionDto;
import org.mapstruct.Mapper;

@Mapper
public interface SessionDtoMapper extends DtoMapper<Session, SessionDto> {
}
