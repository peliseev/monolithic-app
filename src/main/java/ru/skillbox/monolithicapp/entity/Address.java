package ru.skillbox.monolithicapp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "customers")
@ToString(exclude = "customers")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "house", nullable = false)
    private String house;

    @Column(name = "flat", nullable = false)
    private String flat;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
    private Set<Customer> customers = new HashSet<>(0);

}
