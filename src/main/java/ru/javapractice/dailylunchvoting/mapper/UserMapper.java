package ru.javapractice.dailylunchvoting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.user.to.UserTo;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    UserTo toUserTo(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    User toUser(UserTo userTo);
}
