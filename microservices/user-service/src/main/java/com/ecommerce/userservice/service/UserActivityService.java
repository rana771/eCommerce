package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.response.UserActivityResponse;
import com.ecommerce.userservice.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * User activity tracking service
 */
public interface UserActivityService {
    
    // Activity logging
    void logActivity(Long userId, ActivityType activityType, String description, String details, String ipAddress, String userAgent);
    void logActivity(Long userId, ActivityType activityType, String description);
    void logLoginAttempt(Long userId, boolean success, String ipAddress, String userAgent);
    void logSuspiciousActivity(Long userId, String description, String details, String ipAddress);
    
    // Activity retrieval
    Page<UserActivityResponse> getUserActivities(Long userId, Pageable pageable);
    Page<UserActivityResponse> getUserActivitiesByType(Long userId, ActivityType activityType, Pageable pageable);
    List<UserActivityResponse> getUserActivitiesByDateRange(Long userId, LocalDateTime start, LocalDateTime end);
    List<UserActivityResponse> getRecentActivities(Long userId, int limit);
    
    // Suspicious activity
    List<UserActivityResponse> getSuspiciousActivities(Long userId);
    List<UserActivityResponse> getRecentSuspiciousActivities(int hours);
    void markActivityAsSuspicious(Long activityId);
    void resolveS uspiciousActivity(Long activityId);
    
    // Analytics
    long countActivitiesByType(Long userId, ActivityType activityType);
    Map<ActivityType, Long> getActivityDistribution(Long userId);
    Map<String, Long> getActivityTrend(Long userId, int days);
    List<UserActivityResponse> getMostFrequentActivities(Long userId, int limit);
    
    // Security monitoring
    List<UserActivityResponse> getFailedLoginAttempts(Long userId, int hours);
    boolean hasRecentSuspiciousActivity(Long userId, int hours);
    void analyzeUserBehavior(Long userId);
    
    // Cleanup
    void cleanupOldActivities(int daysOld);
}
