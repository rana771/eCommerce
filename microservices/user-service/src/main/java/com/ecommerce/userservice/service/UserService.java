package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.*;
import com.ecommerce.userservice.dto.response.AuthResponse;
import com.ecommerce.userservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for User operations
 */
public interface UserService {
    
    /**
     * Register a new user
     */
    UserResponse register(RegisterUserRequest request);
    
    /**
     * Login user and generate tokens
     */
    AuthResponse login(LoginRequest request);
    
    /**
     * Refresh access token using refresh token
     */
    AuthResponse refreshToken(String refreshToken);
    
    /**
     * Logout user (invalidate tokens)
     */
    void logout(Long userId);
    
    /**
     * Get user by ID
     */
    UserResponse getUserById(Long id);
    
    /**
     * Get user by email
     */
    UserResponse getUserByEmail(String email);
    
    /**
     * Update user profile
     */
    UserResponse updateUser(Long id, UpdateUserRequest request);
    
    /**
     * Change password
     */
    void changePassword(Long id, ChangePasswordRequest request);
    
    /**
     * Request password reset
     */
    void requestPasswordReset(String email);
    
    /**
     * Reset password using token
     */
    void resetPassword(String token, String newPassword);
    
    /**
     * Verify email using token
     */
    void verifyEmail(String token);
    
    /**
     * Resend email verification
     */
    void resendEmailVerification(String email);
    
    /**
     * Enable two-factor authentication
     */
    String enableTwoFactor(Long userId);
    
    /**
     * Disable two-factor authentication
     */
    void disableTwoFactor(Long userId);
    
    /**
     * Verify two-factor code
     */
    boolean verifyTwoFactorCode(Long userId, String code);
    
    /**
     * Get all users (paginated)
     */
    Page<UserResponse> getAllUsers(Long companyId, Pageable pageable);
    
    /**
     * Get users by company ID
     */
    Page<UserResponse> getUsersByCompany(Long companyId, Pageable pageable);
    
    /**
     * Soft delete user
     */
    void softDeleteUser(Long id);
    
    /**
     * Permanently delete user
     */
    void deleteUser(Long id);
    
    /**
     * Lock user account
     */
    void lockAccount(Long id);
    
    /**
     * Unlock user account
     */
    void unlockAccount(Long id);
    
    /**
     * Update user status
     */
    void updateStatus(Long id, String status);
}
