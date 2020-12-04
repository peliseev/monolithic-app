package ru.skillbox.monolithicapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import ru.skillbox.monolithicapp.model.EUserRole;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private EUserRole name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Collection<User> users;

    @Override
    public String getAuthority() {
        return getName().name();
    }

    public EUserRole getName() {
        return this.name;
    }
}
