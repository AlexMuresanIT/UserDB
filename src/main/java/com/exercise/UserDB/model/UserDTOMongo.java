package com.exercise.UserDB.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOMongo {

    private String id;
    private String name;
    private String email;
    private Address address;

    public UserDTOMongo(UserMongo user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserDTOMongo{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", town='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
