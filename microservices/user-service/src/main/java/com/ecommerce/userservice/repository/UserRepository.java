package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    /**
     * Find user by email (case-insensitive)
     */
    Optional<User> findByEmailIgnoreCase(String email);
    
    /**
     * Find user by username (case-insensitive)
     */
    Optional<User> findByUsernameIgnoreCase(String username);
    
    /**
     * Find user by email and company ID
     */
    Optional<User> findByEmailAndCompanyId(String email, Long companyId);
    
    /**
     * Find all users by company ID
     */
    List<User> findByCompanyId(Long companyId);
    
    /**
     * Find user by ID and company ID (for multi-tenancy)
     */
    Optional<User> findByIdAndCompanyId(Long id, Long companyId);
    
    /**
     * Find user by password reset token
     */
    Optional<User> findByPasswordResetToken(String token);
    
    /**
     * Find user by email verification token
     */
    Optional<User> findByEmailVerificationToken(String token);
    
    /**
     * Find user by refresh token
     */
    Optional<User> findByRefreshToken(String refreshToken);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if user exists by ID and company ID
     */
    boolean existsByIdAndCompanyId(Long id, Long companyId);
    
    /**
     * Find all active users by company ID
     */
    @Query("SELECT u FROM User u WHERE u.companyId = :companyId AND u.status = 'ACTIVE' AND u.deletedAt IS NULL")
    List<User> findActiveUsersByCompanyId(@Param("companyId") Long companyId);
    
    /**
     * Find users by status
     */
    List<User> findByStatus(UserStatus status);
    
    /**
     * Update last login time
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :loginTime WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);
    
    /**
     * Lock account
     */
    @Modifying
    @Query("UPDATE User u SET u.accountLockedUntil = :lockUntil, u.failedLoginAttempts = :attempts WHERE u.id = :userId")
    void lockAccount(@Param("userId") Long userId, @Param("lockUntil") LocalDateTime lockUntil, @Param("attempts") Integer attempts);
    
    /**
     * Unlock account
     */
    @Modifying
    @Query("UPDATE User u SET u.accountLockedUntil = NULL, u.failedLoginAttempts = 0 WHERE u.id = :userId")
    void unlockAccount(@Param("userId") Long userId);
    
    /**
     * Verify email
     */
    @Modifying
    @Query("UPDATE User u SET u.emailVerified = true, u.emailVerificationToken = NULL, u.emailVerificationExpires = NULL WHERE u.id = :userId")
    void verifyEmail(@Param("userId") Long userId);
    
    /**
     * Find users with expired password reset tokens
     */
    @Query("SELECT u FROM User u WHERE u.passwordResetExpires < :now AND u.passwordResetToken IS NOT NULL")
    List<User> findUsersWithExpiredPasswordResetTokens(@Param("now") LocalDateTime now);
    
    /**
     * Clean expired tokens
     */
    @Modifying
    @Query("UPDATE User u SET u.passwordResetToken = NULL, u.passwordResetExpires = NULL WHERE u.passwordResetExpires < :now")
    void cleanExpiredPasswordResetTokens(@Param("now") LocalDateTime now);
    
    /**
     * Find users by role (requires join on user_roles table)
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);
}
