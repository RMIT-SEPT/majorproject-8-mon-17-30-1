package com.rmit.sept.septbackend.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
// These mean that password is not included in toString(), equals(), or hashCode() calls
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
// Name maps to the table in the database
@Entity(name = "user")
public class UserEntity {
    // Id means that Hibernate knows which column is the primary key
    @Id
    // This means that Hibernate generates the userId - it is not provided by users nor managed by the database
    @GeneratedValue
    private int userId;
    // The @Column annotation isn't strictly necessary, unless providing extra information about a column, such as here
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    // This joins to the 'Role' table so that the role can be seen
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private RoleEntity role;
}
