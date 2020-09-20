package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.Role;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
// These mean that password is not included in toString(), equals(), or hashCode() calls
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
// Name maps to the table in the database
@Entity
@Table(name = "user")
public class UserEntity {
    // Id means that Hibernate knows which column is the primary key
    // This means that Hibernate generates the userId - it is not provided by users nor managed by the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    // The @Column annotation isn't strictly necessary, unless providing extra information about a column, such as here
    @NaturalId
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity(String username, String password, String firstName, String lastName, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

}
