package com.rmit.sept.septbackend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class User {

    @Id
    private int userId;
    private String firstName;
    private String lastName;

}
