package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.Role;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private Role role;

}
