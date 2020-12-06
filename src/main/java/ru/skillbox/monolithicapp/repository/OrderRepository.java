package ru.skillbox.monolithicapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.monolithicapp.entity.Order;
import ru.skillbox.monolithicapp.model.EOrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("select distinct o from Order o " +
            "join fetch o.items i " +
            "join fetch i.item " +
            "where o.customerId = :customerId")
    List<Order> getByCustomerId(@Param("customerId") Integer customerId);

    @Query("select distinct o from Order o " +
            "join fetch o.items i " +
            "join fetch o.customer " +
            "join fetch i.item " +
            "where o.status = :status1 "+
            "or (o.status = :status2 " +
            "and o.courierId = :courierId)")
    List<Order> findByStatusAndCourierId(@Param("status1") EOrderStatus status1,
                             @Param("status2") EOrderStatus status2,
                             @Param("courierId") Integer courierId);

}
