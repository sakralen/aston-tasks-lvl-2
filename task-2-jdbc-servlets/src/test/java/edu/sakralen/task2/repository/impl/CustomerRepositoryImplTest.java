package edu.sakralen.task2.repository.impl;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.model.SessionCustomer;
import edu.sakralen.task2.repository.CustomerRepository;
import edu.sakralen.task2.repository.SessionCustomerRepository;
import edu.sakralen.task2.repository.SessionRepository;
import edu.sakralen.task2.testutil.DatabaseTestExtension;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryImplTest extends DatabaseTestExtension {
    private static final CustomerRepository<Long> REPOSITORY = new CustomerRepositoryImpl();

    @Test
    void givenDefaultCustomer_whenSaved_thenReturnedWithId() {
        Customer customer = new Customer();

        assertNotNull(REPOSITORY.save(customer));
    }

    @Test
    void givenWithParams_whenSaved_thenReturnedWithId() {
        Customer customer = new Customer("hello", "world");

        assertNotNull(REPOSITORY.save(customer));
    }

    @Test
    void whenSaved_thenFoundById() {
        Customer customer = new Customer("hello", "world");

        Long id = REPOSITORY.save(customer);

        assertNotNull(id);
    }

    @Test
    void givenSaved_whenFoundAll_thenReturnsCorrectCount() {
        int initialCount = REPOSITORY.findAll().size();
        REPOSITORY.save(new Customer());

        assertEquals(initialCount + 1, REPOSITORY.findAll().size());
    }

    @Test
    void whenSaved_thenCountIncreased() {
        int initialCount = REPOSITORY.findAll().size();

        Customer customer = new Customer("hello", "world");
        REPOSITORY.save(customer);

        assertTrue(REPOSITORY.findAll().size() > initialCount);
    }

    @Test
    void whenSaved_thenFoundEqualsInitial() {
        Customer initial = new Customer("hello", "world");

        Long id = REPOSITORY.save(initial);
        initial.setId(id);

        Customer found = REPOSITORY.findById(id);

        assertEquals(initial, found);
    }

    @Test
    void givenSaved_whenDeleted_thenReturnedTrue() {
        Customer customer = new Customer("hello", "world");

        Long id = REPOSITORY.save(customer);

        assertTrue(REPOSITORY.deleteById(id));
    }

    @Test
    void whenDeletedNotPresent_thenReturnedFalse() {
        assertFalse(REPOSITORY.deleteById(Long.MAX_VALUE));
    }

    @Test
    void givenSaved_whenDeleted_thenNotFoundById() {
        Customer customer = new Customer("hello", "world");
        Long id = REPOSITORY.save(customer);

        REPOSITORY.deleteById(id);

        assertNull(REPOSITORY.findById(id));
    }

    @Test
    void whenUpdatedNotPresent_thenReturnsFalse() {
        assertFalse(REPOSITORY.update(new Customer(Long.MAX_VALUE, "hello", "world")));
    }

    @Test
    void givenSaved_whenUpdated_thenReturnedCustomerWithNewAttributes() {
        Customer initial = new Customer("hello", "world");
        Long id = REPOSITORY.save(initial);
        initial.setId(id);

        initial.setName("name");
        initial.setSurname("surname");
        REPOSITORY.update(initial);
        Customer foundAfterUpdate = REPOSITORY.findById(id);

        assertEquals(initial, foundAfterUpdate);
    }

    @Test
    void givenNullId_whenUpdated_thenReturnsNull() {
        assertFalse(REPOSITORY.update(new Customer()));
    }

    @Test
    void givenSavedCustomerAndAttendedSessions_whenFoundByIf_thenReturnedWithSessions() {
        Customer customer = new Customer();
        Long customerId = REPOSITORY.save(customer);
        customer.setId(customerId);

        SessionRepository<Long> sessionRepository = new SessionRepositoryImpl();
        Session session = new Session();
        Long sessionId = sessionRepository.save(session);
        session.setId(sessionId);

        SessionCustomerRepository<Long> sessionCustomerRepository = new SessionCustomerRepositoryImpl();
        SessionCustomer sessionCustomer = new SessionCustomer();
        sessionCustomer.setCustomer(customer);
        sessionCustomer.setSession(session);
        sessionCustomerRepository.save(sessionCustomer);

        assertTrue(REPOSITORY.findByIdWithSessions(customerId).getSessions().contains(session));
    }
}
