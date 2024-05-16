package com.exercise.UserDB.config;

import java.util.List;

public interface ConvertInterface<T, S> {

    T convertToDTO(S user);

    List<T> convertToDTO(List<S> users);
}
