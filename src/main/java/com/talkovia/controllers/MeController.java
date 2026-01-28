package com.talkovia.controllers;

import com.talkovia.dto.MeResponseDTO;
import com.talkovia.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@Tag(name = "Me")
public class MeController {
    @GetMapping
    public ResponseEntity<MeResponseDTO> me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        MeResponseDTO meResponseDTO = new MeResponseDTO(user.getUsername());
        return ResponseEntity.ok(meResponseDTO);
    }
}
