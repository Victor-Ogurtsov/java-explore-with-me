package ru.practicum.model.compilation;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.event.Event;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "compilations_events",
            joinColumns = { @JoinColumn(name = "compilation_id") },
            inverseJoinColumns = { @JoinColumn(name = "event_id") })
    Set<Event> events = new HashSet<Event>();
    @Column(name = "pinned")
    Boolean pinned = false;
    @Column(name = "title")
    String title;
}
/*
@Entity
public class Store {

    @ManyToMany
    @JoinTable(name = “store_product”,
           joinColumns = { @JoinColumn(name = “fk_store”) },
           inverseJoinColumns = { @JoinColumn(name = “fk_product”) })
    private Set<Product> products = new HashSet<Product>();

    …
}

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned boolean,
    title varchar(50) );

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT,
    event_id BIGINT,
    CONSTRAINT PK_compilations_events PRIMARY KEY (compilation_id, event_id) );
 */