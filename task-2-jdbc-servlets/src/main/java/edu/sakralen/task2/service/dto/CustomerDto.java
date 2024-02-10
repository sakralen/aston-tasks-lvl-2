package edu.sakralen.task2.service.dto;

import edu.sakralen.task2.model.Session;

import java.util.Set;

public record CustomerDto(Long id,
                          String name,
                          String surname,
                          Set<Session> sessions) {
}
