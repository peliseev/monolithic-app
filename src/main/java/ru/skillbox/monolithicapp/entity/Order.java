package ru.skillbox.monolithicapp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"customer", "items"})
@ToString(exclude = "customer")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_item",
            joinColumns = { @JoinColumn(name = "order_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "item_id", nullable = false, updatable = false) }
    )
    private Set<Item> items = new HashSet<>(0);

}
