package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.LoginRequest;
import com.ecommerce.userservice.dto.request.RegisterRequest;
import com.ecommerce.userservice.dto.response.AuthResponse;
import com.ecommerce.userservice.dto.response.UserResponse;

/**
 * Authentication service interface
 */
public interface AuthenticationService {
    
    // Registration
    UserResponse register(RegisterRequest request);
    AuthResponse registerAndLogin(RegisterRequest request);
    
    // Login/Logout
    AuthResponse login(LoginRequest request);
    AuthResponse loginWithTwoFactor(String email, String twoFactorCode);
    void logout(String sessionToken);
    void logoutAll(Long userId);
    void logoutAllDevices(Long userId);
    
    // Token management
    AuthResponse refreshToken(String refreshToken);
    void revokeToken(String token);
    void revokeAllTokens(Long userId);
    boolean validateToken(String token);
    
    // Email verification
    void sendVerificationEmail(Long userId);
    UserResponse verifyEmail(String token);
    void resendVerificationEmail(String email);
    
    // Password management
    void initiatePasswordReset(String email);
    UserResponse resetPassword(String token, String newPassword);
    void changePassword(Long userId, String oldPassword, String newPassword);
    boolean validatePassword(String password);
    
    // Two-factor authentication
    String enableTwoFactor(Long userId);
    void disableTwoFactor(Long userId, String code);
    boolean verifyTwoFactorCode(Long userId, String code);
    String generateBackupCodes(Long userId);
    
    // Account security
    boolean isAccountLocked(Long userId);
    void unlockAccount(Long userId);
    void lockAccount(Long userId, String reason);
    void recordFailedLoginAttempt(String email, String ipAddress);
    void recordSuccessfulLogin(Long userId, String ipAddress, String userAgent);
    
    // Session validation
    boolean isSessionValid(String sessionToken);
    void updateSessionActivity(String sessionToken);
    void terminateSession(String sessionToken, String reason);
}
