package edu.sakralen.task2.model;

import edu.sakralen.task2.util.LocalDateTimeUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

public class Session {
    private Long id;
    private LocalDateTime attendedAt;
    private BigDecimal price;
    private Movie movie;
    private Set<Customer> customers;

    public Session() {
    }

    public Session(LocalDateTime attendedAt, BigDecimal price) {
        this.attendedAt = attendedAt;
        this.price = price;
    }

    public Session(Long id, LocalDateTime attendedAt, BigDecimal price) {
        this.id = id;
        this.attendedAt = attendedAt;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAttendedAt() {
        return attendedAt;
    }

    public void setAttendedAt(LocalDateTime attendedAt) {
        this.attendedAt = attendedAt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", attendedAt=" + attendedAt +
                ", price=" + price +
                ", movie=" + movie +
                ", customers=" + customers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id)
                && LocalDateTimeUtils.isEqualTruncated(attendedAt, session.attendedAt, ChronoUnit.SECONDS)
                && Objects.equals(price, session.price)
                && Objects.equals(movie, session.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attendedAt, price, movie);
    }
}
