package edu.sakralen.task2.service.impl;

import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.repository.SessionRepository;
import edu.sakralen.task2.repository.impl.SessionRepositoryImpl;
import edu.sakralen.task2.service.AbstractService;
import edu.sakralen.task2.service.SessionService;
import edu.sakralen.task2.service.dto.SessionDto;
import edu.sakralen.task2.service.dto.mapper.DtoMapper;
import edu.sakralen.task2.service.dto.mapper.SessionDtoMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class SessionServiceImpl extends AbstractService<Session, SessionDto> implements SessionService<Long> {
    private final SessionRepository<Long> sessionRepository;

    public SessionServiceImpl() {
        super(Mappers.getMapper(SessionDtoMapper.class));
        this.sessionRepository = new SessionRepositoryImpl();
    }

    public SessionServiceImpl(DtoMapper<Session, SessionDto> mapper, SessionRepository<Long> sessionRepository) {
        super(mapper);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<SessionDto> findAll() {
        return sessionRepository.findAllWithMovie().stream().map(mapper::toDto).toList();
    }

    @Override
    public SessionDto findById(Long id) {
        if (id == null || id == 0) {
            throw new IllegalArgumentException();
        }

        return mapper.toDto(sessionRepository.findByIdWithMovie(id));
    }

    @Override
    public Long getCustomersCountAtSession(Long id) {
        if (id == null || id == 0) {
            throw new IllegalArgumentException();
        }

        return sessionRepository.countCustomersOnSession(id);
    }
}
