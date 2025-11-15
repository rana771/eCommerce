package com.ecommerce.userservice.enums;

/**
 * Login status enum - tracking login attempts
 */
public enum LoginStatus {
    SUCCESS,
    FAILED_INVALID_CREDENTIALS,
    FAILED_ACCOUNT_LOCKED,
    FAILED_ACCOUNT_SUSPENDED,
    FAILED_EMAIL_NOT_VERIFIED,
    FAILED_TWO_FACTOR_REQUIRED,
    FAILED_TWO_FACTOR_INVALID,
    LOGOUT
}
