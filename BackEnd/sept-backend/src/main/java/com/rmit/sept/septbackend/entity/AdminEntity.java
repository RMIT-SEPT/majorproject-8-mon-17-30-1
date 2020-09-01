package com.rmit.sept.septbackend.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "admin")
public class AdminEntity {
    @Id
    @GeneratedValue
    private int adminId;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "business_id", referencedColumnName = "business_id")
    private BusinessEntity business;
}

