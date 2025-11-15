package com.ecommerce.userservice.enums;

/**
 * Activity type enum - user activity tracking
 */
public enum ActivityType {
    // Authentication
    LOGIN,
    LOGOUT,
    PASSWORD_CHANGED,
    PASSWORD_RESET_REQUESTED,
    EMAIL_VERIFIED,
    TWO_FACTOR_ENABLED,
    TWO_FACTOR_DISABLED,
    
    // Profile
    PROFILE_UPDATED,
    PROFILE_IMAGE_CHANGED,
    PHONE_VERIFIED,
    
    // Account
    ACCOUNT_CREATED,
    ACCOUNT_SUSPENDED,
    ACCOUNT_REACTIVATED,
    ACCOUNT_DELETED,
    
    // Security
    FAILED_LOGIN_ATTEMPT,
    ACCOUNT_LOCKED,
    ACCOUNT_UNLOCKED,
    SUSPICIOUS_ACTIVITY_DETECTED,
    
    // Permissions
    ROLE_ADDED,
    ROLE_REMOVED,
    PERMISSION_GRANTED,
    PERMISSION_REVOKED,
    
    // Sessions
    SESSION_STARTED,
    SESSION_EXPIRED,
    SESSION_TERMINATED,
    
    // Other
    PREFERENCES_UPDATED,
    NOTIFICATION_SETTINGS_CHANGED,
    API_KEY_GENERATED,
    API_KEY_REVOKED
}
