package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.*;
import com.ecommerce.userservice.dto.response.AuthResponse;
import com.ecommerce.userservice.dto.response.UserResponse;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserRole;
import com.ecommerce.userservice.entity.UserStatus;
import com.ecommerce.userservice.entity.UserType;
import com.ecommerce.userservice.exception.*;
import com.ecommerce.userservice.mapper.UserMapper;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of UserService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered: " + request.getEmail());
        }
        
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken: " + request.getUsername());
        }
        
        // Create user entity
        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setType(UserType.valueOf(request.getUserType().toUpperCase()));
        user.setStatus(UserStatus.PENDING_VERIFICATION);
        
        // Assign default role based on user type
        Set<UserRole> roles = new HashSet<>();
        switch (user.getType()) {
            case CUSTOMER -> roles.add(UserRole.ROLE_CUSTOMER);
            case VENDOR -> roles.add(UserRole.ROLE_VENDOR);
            case SHOPPER -> roles.add(UserRole.ROLE_SHOPPER);
            case ADMIN -> roles.add(UserRole.ROLE_ADMIN);
            default -> roles.add(UserRole.ROLE_CUSTOMER);
        }
        user.setRoles(roles);
        
        // Generate email verification token
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerificationExpires(LocalDateTime.now().plusHours(24));
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        // TODO: Send verification email via Kafka event
        
        return userMapper.toResponse(savedUser);
    }
    
    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for: {}", request.getEmailOrUsername());
        
        // Find user by email or username
        User user = userRepository.findByEmailIgnoreCase(request.getEmailOrUsername())
                .or(() -> userRepository.findByUsernameIgnoreCase(request.getEmailOrUsername()))
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));
        
        // Check if account is locked
        if (user.isAccountLocked()) {
            throw new AuthenticationException("Account is locked. Try again later.");
        }
        
        // Check if account is active
        if (!user.isActive()) {
            throw new AuthenticationException("Account is not active");
        }
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            user.incrementFailedLoginAttempts();
            userRepository.save(user);
            throw new AuthenticationException("Invalid credentials");
        }
        
        // Reset failed login attempts
        user.resetFailedLoginAttempts();
        user.setLastLogin(LocalDateTime.now());
        
        // Generate tokens
        Set<String> roles = userMapper.mapRoles(user.getRoles());
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(), 
                user.getEmail(), 
                user.getCompanyId(), 
                roles
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail());
        
        // Save refresh token
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpires(LocalDateTime.now().plusDays(7));
        userRepository.save(user);
        
        log.info("User logged in successfully: {}", user.getEmail());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTimeInSeconds())
                .user(userMapper.toResponse(user))
                .build();
    }
    
    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Refreshing access token");
        
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationException("Invalid refresh token");
        }
        
        String tokenType = jwtTokenProvider.getTokenType(refreshToken);
        if (!"REFRESH".equals(tokenType)) {
            throw new AuthenticationException("Invalid token type");
        }
        
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException("Invalid refresh token"));
        
        if (user.getRefreshTokenExpires().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Refresh token expired");
        }
        
        // Generate new tokens
        Set<String> roles = userMapper.mapRoles(user.getRoles());
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                user.getId(), 
                user.getEmail(), 
                user.getCompanyId(), 
                roles
        );
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail());
        
        // Update refresh token
        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpires(LocalDateTime.now().plusDays(7));
        userRepository.save(user);
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTimeInSeconds())
                .user(userMapper.toResponse(user))
                .build();
    }
    
    @Override
    @Transactional
    public void logout(Long userId) {
        log.info("Logging out user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setRefreshToken(null);
        user.setRefreshTokenExpires(null);
        userRepository.save(user);
        
        log.info("User logged out successfully: {}", userId);
    }
    
    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user by ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        return userMapper.toResponse(user);
    }
    
    @Override
    @Cacheable(value = "users", key = "#email")
    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        return userMapper.toResponse(user);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        userMapper.updateEntityFromRequest(request, user);
        User updated = userRepository.save(user);
        
        log.info("User updated successfully: {}", id);
        return userMapper.toResponse(updated);
    }
    
    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequest request) {
        log.info("Changing password for user: {}", id);
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidOperationException("Passwords do not match");
        }
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new AuthenticationException("Current password is incorrect");
        }
        
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        log.info("Password changed successfully for user: {}", id);
    }
    
    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        log.info("Password reset requested for: {}", email);
        
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpires(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
        
        // TODO: Send password reset email via Kafka event
        
        log.info("Password reset token generated for: {}", email);
    }
    
    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        log.info("Resetting password with token");
        
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new InvalidOperationException("Invalid or expired reset token"));
        
        if (user.getPasswordResetExpires().isBefore(LocalDateTime.now())) {
            throw new InvalidOperationException("Reset token has expired");
        }
        
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        userRepository.save(user);
        
        log.info("Password reset successfully for user: {}", user.getEmail());
    }
    
    @Override
    @Transactional
    public void verifyEmail(String token) {
        log.info("Verifying email with token");
        
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new InvalidOperationException("Invalid or expired verification token"));
        
        if (user.getEmailVerificationExpires().isBefore(LocalDateTime.now())) {
            throw new InvalidOperationException("Verification token has expired");
        }
        
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpires(null);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        
        log.info("Email verified successfully for user: {}", user.getEmail());
    }
    
    @Override
    @Transactional
    public void resendEmailVerification(String email) {
        log.info("Resending email verification for: {}", email);
        
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getEmailVerified()) {
            throw new InvalidOperationException("Email already verified");
        }
        
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerificationExpires(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        
        // TODO: Send verification email via Kafka event
        
        log.info("Verification email resent for: {}", email);
    }
    
    @Override
    @Transactional
    public String enableTwoFactor(Long userId) {
        log.info("Enabling 2FA for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        String secret = UUID.randomUUID().toString(); // In production, use Google Authenticator secret
        user.setTwoFactorSecret(secret);
        user.setTwoFactorEnabled(true);
        userRepository.save(user);
        
        log.info("2FA enabled for user: {}", userId);
        return secret;
    }
    
    @Override
    @Transactional
    public void disableTwoFactor(Long userId) {
        log.info("Disabling 2FA for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setTwoFactorSecret(null);
        user.setTwoFactorEnabled(false);
        userRepository.save(user);
        
        log.info("2FA disabled for user: {}", userId);
    }
    
    @Override
    public boolean verifyTwoFactorCode(Long userId, String code) {
        log.debug("Verifying 2FA code for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // TODO: Implement actual TOTP verification with Google Authenticator
        return user.getTwoFactorSecret() != null && !code.isEmpty();
    }
    
    @Override
    public Page<UserResponse> getAllUsers(Long companyId, Pageable pageable) {
        log.debug("Fetching all users, companyId: {}", companyId);
        
        Page<User> users = companyId != null 
                ? userRepository.findAll(pageable) 
                : userRepository.findAll(pageable);
        
        return users.map(userMapper::toResponse);
    }
    
    @Override
    public Page<UserResponse> getUsersByCompany(Long companyId, Pageable pageable) {
        log.debug("Fetching users for company: {}", companyId);
        
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toResponse);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void softDeleteUser(Long id) {
        log.info("Soft deleting user: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setDeletedAt(LocalDateTime.now());
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
        
        log.info("User soft deleted: {}", id);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        log.info("Permanently deleting user: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        
        userRepository.deleteById(id);
        log.info("User permanently deleted: {}", id);
    }
    
    @Override
    @Transactional
    public void lockAccount(Long id) {
        log.info("Locking account: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setAccountLockedUntil(LocalDateTime.now().plusDays(365)); // Lock for 1 year
        userRepository.save(user);
        
        log.info("Account locked: {}", id);
    }
    
    @Override
    @Transactional
    public void unlockAccount(Long id) {
        log.info("Unlocking account: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setAccountLockedUntil(null);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
        
        log.info("Account unlocked: {}", id);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void updateStatus(Long id, String status) {
        log.info("Updating status for user {}: {}", id, status);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setStatus(UserStatus.valueOf(status.toUpperCase()));
        userRepository.save(user);
        
        log.info("User status updated: {}", id);
    }
}
