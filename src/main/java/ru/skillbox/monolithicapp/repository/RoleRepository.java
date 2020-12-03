package ru.skillbox.monolithicapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.monolithicapp.entity.UserRole;
import ru.skillbox.monolithicapp.model.EUserRole;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(EUserRole name);
}
