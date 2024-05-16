package com.exercise.UserDB.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "USERS")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "STUDENT_NAME", nullable = false, unique = false)
    private String name;

    @Column(name = "STUDENT_EMAIL",nullable = false, unique = true)
    private String email;

    @Column(name = "STUDENT_PW", nullable = false, unique = false)
    private String password;

    @Column(name = "STUDENT_TOWN", nullable = false, unique = false)
    private String town;
}
