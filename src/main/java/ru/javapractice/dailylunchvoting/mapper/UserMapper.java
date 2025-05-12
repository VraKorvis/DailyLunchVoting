package ru.javapractice.dailylunchvoting.mapper;

import org.mapstruct.*;

import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.user.to.UserTo;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", expression = "java(userTo.getEmail().toLowerCase())")
    @Mapping(target = "roles", expression = "java(java.util.Set.of(ru.javapractice.dailylunchvoting.user.model.Role.USER))")
    User createNewFromTo(UserTo userTo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", expression = "java(userTo.getEmail().toLowerCase())")
    void updateFromTo(@MappingTarget User user, UserTo userTo);
}

