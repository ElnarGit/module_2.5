/*
package com.elnar.module25.mapper;

import com.elnar.module25.dto.UserDto;
import com.elnar.module25.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper2 {
	
	public static UserDto toUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		userDto.setPassword(user.getPassword()); // Assuming this is required in UserDto
		userDto.setRole(user.getRole());
		userDto.setStatus(user.getStatus());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setCreatedAt(user.getCreatedAt());
		userDto.setUpdatedAt(user.getUpdatedAt());
		return userDto;
	}
	
	public static User toUser(UserDto userDto) {
		User user = new User();
		user.setId(userDto.getId());
		user.setUsername(userDto.getUsername());
		user.setPassword(userDto.getPassword()); // Assuming this is required in User
		user.setRole(userDto.getRole());
		user.setStatus(userDto.getStatus());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setCreatedAt(userDto.getCreatedAt());
		user.setUpdatedAt(userDto.getUpdatedAt());
		return user;
	}
}
*/
