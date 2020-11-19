package ru.skillbox.monolithicapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.monolithicapp.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("select distinct o from Order o " +
            "join fetch o.items i " +
            "join fetch i.item " +
            "where o.customerId = :customerId")
    List<Order> getByCustomerId(@Param("customerId") Integer customerId);
}
