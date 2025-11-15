package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.response.UserSessionResponse;
import com.ecommerce.userservice.enums.SessionStatus;

import java.util.List;

/**
 * Session management service
 */
public interface SessionService {
    
    // Session creation
    UserSessionResponse createSession(Long userId, String ipAddress, String userAgent, String deviceType, String deviceId);
    
    // Session retrieval
    UserSessionResponse getSessionById(Long sessionId);
    UserSessionResponse getSessionByToken(String sessionToken);
    List<UserSessionResponse> getUserSessions(Long userId);
    List<UserSessionResponse> getActiveSessions(Long userId);
    List<UserSessionResponse> getSessionsByStatus(Long userId, SessionStatus status);
    
    // Session management
    void updateSessionActivity(String sessionToken);
    void updateSessionActivity(Long sessionId);
    void terminateSession(String sessionToken, String reason);
    void terminateSession(Long sessionId, String reason);
    void terminateAllSessions(Long userId, String reason);
    void terminateOtherSessions(Long userId, String currentSessionToken, String reason);
    
    // Session validation
    boolean isSessionValid(String sessionToken);
    boolean isSessionActive(String sessionToken);
    void refreshSession(String sessionToken);
    
    // Expired session cleanup
    void markExpiredSessions();
    void cleanupExpiredSessions(int daysOld);
    
    // Statistics
    long countActiveSessions(Long userId);
    long countTotalActiveSessions();
    List<UserSessionResponse> getRecentSessions(Long userId, int limit);
    
    // Device management
    UserSessionResponse getSessionByUserAndDevice(Long userId, String deviceId);
    List<UserSessionResponse> getSessionsByDevice(String deviceId);
    void terminateDeviceSessions(String deviceId, String reason);
}
