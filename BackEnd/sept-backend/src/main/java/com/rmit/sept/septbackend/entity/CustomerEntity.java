package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.State;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "customer")
public class CustomerEntity {
    @Id
    @GeneratedValue
    private int customerId;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;
    private String streetAddress;
    private String city;
    @Enumerated(EnumType.STRING)
    private State state;
    private String postcode;
}
