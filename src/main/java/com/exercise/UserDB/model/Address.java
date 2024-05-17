package com.exercise.UserDB.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {

    private String street;
    private Integer number;
    private Integer zipcode;
    private String city;
}
