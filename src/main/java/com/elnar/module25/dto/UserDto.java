package com.elnar.module25.dto;


import com.elnar.module25.entity.Status;
import com.elnar.module25.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
	
	private Long id;
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private UserRole role;
	private Status status;
	private String firstName;
	private String lastName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
