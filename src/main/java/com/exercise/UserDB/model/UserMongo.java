package com.exercise.UserDB.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserMongo {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String town;

}
