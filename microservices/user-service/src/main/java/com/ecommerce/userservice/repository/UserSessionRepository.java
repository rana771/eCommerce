package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.UserSession;
import com.ecommerce.userservice.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    
    Optional<UserSession> findBySessionToken(String sessionToken);
    
    List<UserSession> findByUserIdAndStatus(Long userId, SessionStatus status);
    
    List<UserSession> findByUserId(Long userId);
    
    @Query("SELECT s FROM UserSession s WHERE s.user.id = :userId AND s.status = 'ACTIVE' AND s.expiresAt > :now")
    List<UserSession> findActiveSessionsByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    @Query("SELECT s FROM UserSession s WHERE s.expiresAt < :now AND s.status = 'ACTIVE'")
    List<UserSession> findExpiredSessions(@Param("now") LocalDateTime now);
    
    @Query("SELECT s FROM UserSession s WHERE s.user.id = :userId AND s.deviceId = :deviceId AND s.status = 'ACTIVE'")
    Optional<UserSession> findActiveSessionByUserAndDevice(@Param("userId") Long userId, @Param("deviceId") String deviceId);
    
    long countByUserIdAndStatus(Long userId, SessionStatus status);
    
    @Query("SELECT COUNT(s) FROM UserSession s WHERE s.status = 'ACTIVE' AND s.expiresAt > :now")
    long countActiveSessions(@Param("now") LocalDateTime now);
}
