package com.diabetesapp.controller;

import com.diabetesapp.dto.UserDto;
import com.diabetesapp.security.FirebasePrincipal;
import com.diabetesapp.service.ChatService;
import com.diabetesapp.service.CurrentUserService;
import com.diabetesapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ChatService chatService;
    private final CurrentUserService currentUser;

    @GetMapping
    public ResponseEntity<UserDto.Response> getProfile(@AuthenticationPrincipal FirebasePrincipal me) {
        return ResponseEntity.ok(userService.getProfile(currentUser.id(me)));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto.Response> updateProfile(
            @AuthenticationPrincipal FirebasePrincipal me,
            @Valid @RequestBody UserDto.UpdateRequest request) {
        return ResponseEntity.ok(userService.updateProfile(currentUser.id(me), request));
    }

    @PostMapping("/chat")
    public ResponseEntity<UserDto.ChatResponse> chat(
            @AuthenticationPrincipal FirebasePrincipal me,
            @Valid @RequestBody UserDto.ChatRequest request) {
        currentUser.id(me); // ensures the user row exists
        return ResponseEntity.ok(chatService.chat(request.getMessage()));
    }
}
