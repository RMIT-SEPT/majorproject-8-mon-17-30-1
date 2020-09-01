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
    @GeneratedValue
    @Column(name = "business_id")
    private int businessId;
    private String businessName;
}

