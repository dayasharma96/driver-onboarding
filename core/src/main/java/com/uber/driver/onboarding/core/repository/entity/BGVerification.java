package com.uber.driver.onboarding.core.repository.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

//@Entity
//@Table(name = "bg_verification",
//        indexes = {
//                @Index(name = "emailType", columnList = "email, user_type", unique = true)
//        }
//)
public class BGVerification implements Serializable {

    private static final long serialVersionUID = 2582139718221398431L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(updatable = false, length = 36)
    private String id;

}
