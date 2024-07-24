package com.ds4u.schedulingApis.entity;

import javax.persistence.*;

@Entity
@Table(name = "insurance")
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="insurance_type")
    private String insuranceType;


}
