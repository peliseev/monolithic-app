package ru.skillbox.monolithicapp.entity.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import ru.skillbox.monolithicapp.entity.Customer;
import ru.skillbox.monolithicapp.model.ERole;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private ERole name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "customer_roles",
            joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = { @JoinColumn(name = "customer_id", referencedColumnName = "id")}
    )
    private Collection<Customer> customers;

    @Override
    public String getAuthority() {
        return getName().name();
    }

    public ERole getName() {
        return this.name;
    }
}
