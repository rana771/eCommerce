package com.ecommerce.userservice.entity;

import com.ecommerce.userservice.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * User session entity - tracks active user sessions
 */
@Entity
@Table(name = "user_sessions", indexes = {
    @Index(name = "idx_session_user_id", columnList = "user_id"),
    @Index(name = "idx_session_token", columnList = "session_token"),
    @Index(name = "idx_session_status", columnList = "status"),
    @Index(name = "idx_session_expires_at", columnList = "expires_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "session_token", nullable = false, unique = true, length = 500)
    private String sessionToken;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SessionStatus status = SessionStatus.ACTIVE;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "device_type", length = 50)
    private String deviceType; // web, mobile, tablet

    @Column(name = "device_id", length = 255)
    private String deviceId;

    @Column(name = "location", length = 255)
    private String location; // City, Country

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "terminated_at")
    private LocalDateTime terminatedAt;

    @Column(name = "terminated_reason", length = 255)
    private String terminatedReason;

    // Helper methods
    public boolean isActive() {
        return status == SessionStatus.ACTIVE && 
               expiresAt.isAfter(LocalDateTime.now()) &&
               terminatedAt == null;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now()) ||
               status == SessionStatus.EXPIRED;
    }

    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    public void terminate(String reason) {
        this.status = SessionStatus.TERMINATED;
        this.terminatedAt = LocalDateTime.now();
        this.terminatedReason = reason;
    }
}
