package com.elnar.module25.mapper;

import com.elnar.module25.dto.UserDto;
import com.elnar.module25.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto map(User user);

  @InheritInverseConfiguration
  User map(UserDto dto);
}
