package com.rmit.sept.septbackend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private int businessId;
    private String businessName;
}

