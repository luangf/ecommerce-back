package com.talkovia.controllers;

import com.talkovia.dto.auth.ForgotRequestDTO;
import com.talkovia.dto.auth.LoginRequestDTO;
import com.talkovia.dto.auth.RegisterRequestDTO;
import com.talkovia.services.AuthService;
import com.talkovia.services.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
//	private final EmailService emailService;
    private final CookieService cookieService;
	
	@Operation(summary = "Login an existing user")
	@PostMapping("/login")
	public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        String token = authService.login(loginRequestDTO);
        cookieService.generateCookieWithJWT(token, response);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Register a new user")
	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO, HttpServletResponse response) {
        String token = authService.register(registerRequestDTO);
        cookieService.generateCookieWithJWT(token, response);
		return ResponseEntity.status(CREATED).build();
	}

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieService.expirateCookie(response);
        return ResponseEntity.noContent().build();
    }
	
	@Operation(summary = "Forgot password")
	@PostMapping("/forgot")
	public ResponseEntity<Void> forgot(@Valid @RequestBody ForgotRequestDTO forgotRequestDTO) {
//		emailService.sendEmail(forgotRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(summary = "OAuth")
	@GetMapping("/oauth2/success")
	public ResponseEntity<?> oauthSuccess(@AuthenticationPrincipal OAuth2User principal) {
		String email = principal.getAttribute("email");
		return ResponseEntity.ok("Bem-vindo, " + email);
	}
}
