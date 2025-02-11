package ru.skillmate.backend.controllers.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skillmate.backend.annotations.validation.Trimmed;
import ru.skillmate.backend.dto.users.request.ConfirmEmailRequestDto;
import ru.skillmate.backend.dto.users.request.PendingUserRequestDto;
import ru.skillmate.backend.dto.users.request.UserLoginRequestDto;
import ru.skillmate.backend.dto.users.response.PendingUserResponseDto;
import ru.skillmate.backend.dto.users.response.UserResponseDto;
import ru.skillmate.backend.services.users.UsersAuthService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auth")
@Validated
@Tag(name = "UsersAuthController", description = "Endpoints for authentication")
public class UsersAuthController {
    private final UsersAuthService userAuthService;

    @PostMapping("/register")
    @Operation(summary = "Endpoint to make a register request")
    public ResponseEntity<PendingUserResponseDto> register(@RequestBody @Valid PendingUserRequestDto registerRequestDto) {
        PendingUserResponseDto pendingUserResponseDto = userAuthService.registerRequest(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pendingUserResponseDto);
    }

    @PostMapping("/confirm-email")
    @Operation(summary = "Endpoint to confirm email via code")
    public ResponseEntity<UserResponseDto> confirmEmail(@RequestBody @Valid ConfirmEmailRequestDto requestDto) {
        UserResponseDto responseDto = userAuthService.confirmEmail(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/resend-code")
    @Operation(summary = "Endpoint to resend confirmation code")
    public ResponseEntity<Map<String, String>> resendCode(@RequestParam @Trimmed @Email(message = "Invalid email format") String email) {
        userAuthService.resendCode(email);
        return ResponseEntity.ok().body(Map.of("message", "Confirmation code was sent"));
    }

    @PostMapping("/login")
    @Operation(summary = "Endpoint to make a login request")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        UserResponseDto userResponseDto = userAuthService.login(userLoginRequestDto, response);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping("/logout")
    @Operation(summary = "Endpoint to logout by anulling tokens")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        userAuthService.logout(response);
        return ResponseEntity.noContent().build();
    }
}
