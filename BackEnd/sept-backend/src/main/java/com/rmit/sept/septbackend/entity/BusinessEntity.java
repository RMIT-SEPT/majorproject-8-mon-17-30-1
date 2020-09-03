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
@Table(name = "business")
public class BusinessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_id")
    private int businessId;
    @Column(unique = true)
    private String businessName;

    public BusinessEntity(String businessName) {
        this.businessName = businessName;
    }
}

