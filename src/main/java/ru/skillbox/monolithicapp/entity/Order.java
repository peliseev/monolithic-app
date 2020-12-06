package ru.skillbox.monolithicapp.entity;

import lombok.*;
import ru.skillbox.monolithicapp.model.EOrderStatus;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"customer", "items"})
@ToString(exclude = "customer")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private User courier;

    @Column(name = "customer_id", insertable = false, updatable = false)
    private Integer customerId;

    @Column(name = "courier_id", insertable = false, updatable = false)
    private Integer courierId;

    @Column(name = "status",  nullable = false)
    private EOrderStatus status;

    @Column(name = "total_price",  nullable = false)
    private int totalPrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderItem> items = new LinkedList<>();

}
