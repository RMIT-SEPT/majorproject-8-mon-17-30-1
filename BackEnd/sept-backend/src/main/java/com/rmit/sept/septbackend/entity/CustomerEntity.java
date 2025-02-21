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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;
    private String streetAddress;
    private String city;
    @Enumerated(EnumType.STRING)
    private State state;
    private String postcode;

    public CustomerEntity(UserEntity user, String streetAddress, String city, State state, String postcode) {
        this.user = user;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
    }
}
