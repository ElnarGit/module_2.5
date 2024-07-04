package com.elnar.module25.rest;

import com.elnar.module25.dto.AuthRequestDto;
import com.elnar.module25.dto.AuthResponseDto;
import com.elnar.module25.dto.UserDto;
import com.elnar.module25.entity.User;
import com.elnar.module25.mapper.UserMapper;
import com.elnar.module25.security.CustomPrincipal;
import com.elnar.module25.security.SecurityService;
import com.elnar.module25.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {
	private final SecurityService securityService;
	private final UserService userService;
	private final UserMapper userMapper;
	
	@PostMapping("/register")
	public Mono<UserDto> register(@RequestBody UserDto dto){
		User user = userMapper.map(dto);
		return userService.registerUser(user)
				.map(userMapper::map);
	}
	
	@PostMapping("/login")
	public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto){
		return securityService.authenticate(dto.getUsername(), dto.getPassword())
				.flatMap(tokenDetails -> Mono.just(
						AuthResponseDto.builder()
								.userId(tokenDetails.getUserId())
								.token(tokenDetails.getToken())
								.issuedAt(tokenDetails.getIssuedAt())
								.expiresAt(tokenDetails.getExpiresAt())
								.build()
				));
	}
	
	@GetMapping("/info")
	public Mono<UserDto> getUserInfo(Authentication authentication){
		CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

		return userService.getById(principal.getId())
				.map(userMapper::map);
	}
}
