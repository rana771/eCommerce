package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.UserActivity;
import com.ecommerce.userservice.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    
    Page<UserActivity> findByUserId(Long userId, Pageable pageable);
    
    Page<UserActivity> findByUserIdAndActivityType(Long userId, ActivityType activityType, Pageable pageable);
    
    List<UserActivity> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a FROM UserActivity a WHERE a.user.id = :userId AND a.isSuspicious = true ORDER BY a.createdAt DESC")
    List<UserActivity> findSuspiciousActivitiesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT a FROM UserActivity a WHERE a.isSuspicious = true AND a.createdAt >= :since ORDER BY a.createdAt DESC")
    List<UserActivity> findRecentSuspiciousActivities(@Param("since") LocalDateTime since);
    
    List<UserActivity> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
    
    long countByUserIdAndActivityType(Long userId, ActivityType activityType);
}
