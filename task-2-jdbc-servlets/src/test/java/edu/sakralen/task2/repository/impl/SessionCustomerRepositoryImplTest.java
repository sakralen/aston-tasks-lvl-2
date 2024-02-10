package edu.sakralen.task2.repository.impl;

import edu.sakralen.task2.model.SessionCustomer;
import edu.sakralen.task2.repository.SessionCustomerRepository;
import edu.sakralen.task2.testutil.DatabaseTestExtension;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SessionCustomerRepositoryImplTest extends DatabaseTestExtension {
    private static final SessionCustomerRepository<Long> REPOSITORY = new SessionCustomerRepositoryImpl();

    @Test
    void whenSaved_thenReturnedWithId() {
        assertNotNull(REPOSITORY.save(new SessionCustomer()));
    }

    @Test
    void givenSaved_thenFoundById_ReturnsSavedSession() {
        SessionCustomer sessionCustomer = new SessionCustomer();
        Long id = REPOSITORY.save(sessionCustomer);
        sessionCustomer.setId(id);

        SessionCustomer found = REPOSITORY.findById(id);

        assertEquals(sessionCustomer, found);
    }

    @Test
    void givenSaved_whenFoundAll_thenReturnsCorrectCount() {
        int initialCount = REPOSITORY.findAll().size();
        REPOSITORY.save(new SessionCustomer());

        assertEquals(initialCount + 1, REPOSITORY.findAll().size());
    }

    @Test
    void givenSaved_whenDeleted_thenReturnedTrue() {
        Long id = REPOSITORY.save(new SessionCustomer());

        assertTrue(REPOSITORY.deleteById(id));
    }

    @Test
    void whenDeletedNotPresent_thenReturnedFalse() {
        assertFalse(REPOSITORY.deleteById(Long.MAX_VALUE));
    }


    @Test
    void givenSaved_whenDeleted_thenNotFoundById() {
        Long id = REPOSITORY.save(new SessionCustomer());

        REPOSITORY.deleteById(id);

        assertNull(REPOSITORY.findById(id));
    }

    @Test
    void whenUpdatedNotPresent_thenReturnsFalse() {
        assertFalse(REPOSITORY.update(new SessionCustomer(Long.MAX_VALUE, LocalDateTime.now())));
    }

    @Test
    void givenSaved_whenUpdated_thenReturnedMovieWithNewAttributes() {
        SessionCustomer initial = new SessionCustomer(LocalDateTime.now());
        Long sessionCustomerId = REPOSITORY.save(initial);
        initial.setId(sessionCustomerId);

        SessionCustomer updated = new SessionCustomer(sessionCustomerId, LocalDateTime.now().plusDays(1));

        REPOSITORY.update(updated);

        assertNotEquals(REPOSITORY.findById(sessionCustomerId), initial);
    }
}