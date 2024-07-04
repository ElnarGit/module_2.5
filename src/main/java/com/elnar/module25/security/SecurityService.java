package com.elnar.module25.security;

import com.elnar.module25.entity.Status;
import com.elnar.module25.entity.User;
import com.elnar.module25.exception.AuthException;
import com.elnar.module25.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityService {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Integer expirationInSeconds;
	
	@Value("${jwt.issuer}")
	private String issuer;
	
	private TokenDetails generatedToken(User user){
		Map<String, Object> claims = new HashMap<>() {{
				put("role", user.getRole());
				put("username", user.getUsername());
			}};
		
		return generatedToken(claims, user.getId().toString());
	}
	
	private TokenDetails generatedToken(Map<String, Object> claims, String subject){
		Long expirationTimeInMillis = expirationInSeconds * 1000L;
		Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);
		
		return generatedToken(expirationDate, claims, subject);
	}
	
	private TokenDetails generatedToken(Date expirationDate, Map<String, Object> claims, String subject) {
		Date createdDate = new Date();
		
		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		
		String token = Jwts.builder()
				.claims()
				.add(claims)
				.issuer(issuer)
				.subject(subject)
				.issuedAt(createdDate)
				.id(UUID.randomUUID().toString())
				.expiration(expirationDate)
				.and()
				.signWith(key)
				.compact();
		
		return TokenDetails.builder()
				.token(token)
				.issuedAt(createdDate)
				.expiresAt(expirationDate)
				.build();
	}
	
	public Mono<TokenDetails> authenticate(String username, String password){
		return userService.getByUsername(username)
				.flatMap(user -> {
					if(user.getStatus().equals(Status.DELETED)){
						return Mono.error(new AuthException("User deleted", "ELNAR_USER_ACCOUNT-DELETED"));
					}
					
					if(!passwordEncoder.matches(password, user.getPassword())){
						return Mono.error(new AuthException("Invalid password", "INVALID_PASSWORD"));
					}
					
					return Mono.just(generatedToken(user).toBuilder()
							.userId(user.getId())
							.build());
				})
				.switchIfEmpty(Mono.error(new AuthException("User not found", "USER_NOT_FOUND")));
	}
}
