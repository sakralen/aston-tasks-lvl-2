package edu.sakralen.task2.model;

import edu.sakralen.task2.util.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class SessionCustomer {
    private Long id;
    private LocalDateTime registeredAt;
    private Session session;
    private Customer customer;

    public SessionCustomer() {
    }

    public SessionCustomer(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public SessionCustomer(Long id, LocalDateTime registeredAt) {
        this.id = id;
        this.registeredAt = registeredAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "SessionCustomer{" +
                "id=" + id +
                ", registeredAt=" + registeredAt +
                ", session=" + session +
                ", customer=" + customer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionCustomer that = (SessionCustomer) o;
        return Objects.equals(id, that.id)
                && LocalDateTimeUtils.isEqualTruncated(registeredAt, that.registeredAt, ChronoUnit.SECONDS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registeredAt);
    }
}
