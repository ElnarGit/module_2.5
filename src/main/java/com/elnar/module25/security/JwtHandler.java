package com.elnar.module25.security;

import com.elnar.module25.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtHandler {
	private final String secret;
	
	public JwtHandler(String secret) {
		this.secret = secret;
	}
	
	public Mono<VerificationResult> check(String accessToken){
		return Mono.just(verify(accessToken))
				.onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
	}
	
	private VerificationResult verify(String token){
		Claims claims = getClaimsFromToken(token);
		
		final Date exrirationDate = claims.getExpiration();
		
		if(exrirationDate.before(new Date())){
			throw new RuntimeException("Token expired");
		}
		
		return new VerificationResult(claims, token);
	
	}
	
	private Claims getClaimsFromToken(String token){
		SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
		
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public static class VerificationResult {
		public Claims claims;
		public String token;
		
		public VerificationResult(Claims claims, String token) {
			this.claims = claims;
			this.token = token;
		}
	}
}
