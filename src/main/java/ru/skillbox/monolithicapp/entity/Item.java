package ru.skillbox.monolithicapp.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString(exclude = "orders")
@EqualsAndHashCode(exclude = "orders")
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<OrderItem> orders = new HashSet<>();

}
