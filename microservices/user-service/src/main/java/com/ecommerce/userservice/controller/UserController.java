package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.request.*;
import com.ecommerce.userservice.dto.response.AuthResponse;
import com.ecommerce.userservice.dto.response.UserResponse;
import com.ecommerce.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for User operations
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for user registration, authentication, and profile management")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        log.info("REST request to register user: {}", request.getEmail());
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("REST request to login user: {}", request.getEmailOrUsername());
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> request) {
        log.info("REST request to refresh token");
        String refreshToken = request.get("refreshToken");
        AuthResponse response = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(@RequestParam Long userId) {
        log.info("REST request to logout user: {}", userId);
        userService.logout(userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("REST request to get user: {}", id);
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("REST request to get user by email: {}", email);
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("REST request to update user: {}", id);
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/change-password")
    @Operation(summary = "Change password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("REST request to change password for user: {}", id);
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/request-password-reset")
    @Operation(summary = "Request password reset")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody Map<String, String> request) {
        log.info("REST request to reset password");
        String email = request.get("email");
        userService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password with token")
    public ResponseEntity<Void> resetPassword(@RequestBody Map<String, String> request) {
        log.info("REST request to complete password reset");
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/verify-email")
    @Operation(summary = "Verify email with token")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        log.info("REST request to verify email");
        userService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/resend-verification")
    @Operation(summary = "Resend email verification")
    public ResponseEntity<Void> resendVerification(@RequestBody Map<String, String> request) {
        log.info("REST request to resend verification");
        String email = request.get("email");
        userService.resendEmailVerification(email);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/enable-2fa")
    @Operation(summary = "Enable two-factor authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> enableTwoFactor(@PathVariable Long id) {
        log.info("REST request to enable 2FA for user: {}", id);
        String secret = userService.enableTwoFactor(id);
        return ResponseEntity.ok(Map.of("secret", secret));
    }
    
    @PostMapping("/{id}/disable-2fa")
    @Operation(summary = "Disable two-factor authentication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> disableTwoFactor(@PathVariable Long id) {
        log.info("REST request to disable 2FA for user: {}", id);
        userService.disableTwoFactor(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/verify-2fa")
    @Operation(summary = "Verify two-factor code")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Boolean>> verifyTwoFactorCode(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        log.info("REST request to verify 2FA code");
        String code = request.get("code");
        boolean valid = userService.verifyTwoFactorCode(id, code);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
    
    @GetMapping
    @Operation(summary = "Get all users with pagination")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(required = false) Long companyId,
            Pageable pageable) {
        log.info("REST request to get all users");
        Page<UserResponse> response = userService.getAllUsers(companyId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get users by company")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Page<UserResponse>> getUsersByCompany(
            @PathVariable Long companyId,
            Pageable pageable) {
        log.info("REST request to get users for company: {}", companyId);
        Page<UserResponse> response = userService.getUsersByCompany(companyId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Soft delete user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        log.info("REST request to soft delete user: {}", id);
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Permanently delete user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("REST request to permanently delete user: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/lock")
    @Operation(summary = "Lock user account")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<Void> lockAccount(@PathVariable Long id) {
        log.info("REST request to lock account: {}", id);
        userService.lockAccount(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/unlock")
    @Operation(summary = "Unlock user account")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<Void> unlockAccount(@PathVariable Long id) {
        log.info("REST request to unlock account: {}", id);
        userService.unlockAccount(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update user status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        log.info("REST request to update status for user: {}", id);
        String status = request.get("status");
        userService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
