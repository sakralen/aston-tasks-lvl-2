package edu.sakralen.task2.service.dto;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Movie;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record SessionDto(Long id,
                         LocalDateTime dateTime,
                         BigDecimal price,
                         Movie movie,
                         Set<Customer> customers) {
}
