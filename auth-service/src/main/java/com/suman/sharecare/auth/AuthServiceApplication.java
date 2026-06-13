package com.suman.sharecare.auth;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.util.Base64;

//@Slf4j
@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
//		SecretKey secretKey = Jwts.SIG.HS256.key().build();
//		String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(secretKey.getEncoded());
//		log.info("Secret = {}", secret);

		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
