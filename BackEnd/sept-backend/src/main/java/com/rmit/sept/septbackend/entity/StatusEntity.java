package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.Role;
import com.rmit.sept.septbackend.model.Status;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name = "status")
public class StatusEntity {
    @Id
    @GeneratedValue
    private int statusId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private Status status;
}
