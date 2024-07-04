package com.elnar.module25.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
	
	@Id
	private Long id;
	private String username;
	private String password;
	private UserRole role;
	private Status status;
	@Transient
	@ToString.Exclude
	private List<Event> events;
	
	@ToString.Include(name = "password")
	private String maskPassword() {
		return "***********";
	}
}
