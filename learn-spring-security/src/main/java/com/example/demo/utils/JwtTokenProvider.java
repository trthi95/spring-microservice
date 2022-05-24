package com.example.demo.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.demo.userdetail.CustomUserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
	private static final String JWT_KEY = "truong";

	private static final Long JWT_EXPIRATION = 1000000L;

	public String generateToken(CustomUserDetail customUserDetail) {
		Date now = new Date();
		Date expire = new Date(now.getTime() + JWT_EXPIRATION);

		return Jwts.builder().setIssuedAt(now).setExpiration(expire)
				.setSubject(customUserDetail.getUser().getUsername())
				.signWith(SignatureAlgorithm.HS512, JWT_KEY).compact();
	}

	public String getUsernameFromJwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		} catch (SignatureException e) {
			log.error(e.getMessage());
		}
		return false;
	}
}
