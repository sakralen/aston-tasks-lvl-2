package edu.sakralen.task2.service;

import edu.sakralen.task2.service.dto.BookingDto;

public interface BookingService<K> extends Service<K> {
    K bookSession(BookingDto dto);

    boolean unbookSession(BookingDto dto);
}
