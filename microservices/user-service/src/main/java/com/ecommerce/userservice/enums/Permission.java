package com.ecommerce.userservice.enums;

/**
 * Permission enum - fine-grained access control
 */
public enum Permission {
    // Product permissions
    PRODUCT_VIEW,
    PRODUCT_CREATE,
    PRODUCT_UPDATE,
    PRODUCT_DELETE,
    PRODUCT_PUBLISH,
    
    // Order permissions
    ORDER_VIEW,
    ORDER_CREATE,
    ORDER_UPDATE,
    ORDER_CANCEL,
    ORDER_REFUND,
    
    // User permissions
    USER_VIEW,
    USER_CREATE,
    USER_UPDATE,
    USER_DELETE,
    USER_SUSPEND,
    
    // Company permissions
    COMPANY_VIEW,
    COMPANY_UPDATE,
    COMPANY_SETTINGS,
    
    // Financial permissions
    FINANCIAL_VIEW,
    FINANCIAL_REPORTS,
    FINANCIAL_TRANSACTIONS,
    
    // Analytics permissions
    ANALYTICS_VIEW,
    ANALYTICS_EXPORT,
    
    // System permissions
    SYSTEM_SETTINGS,
    SYSTEM_LOGS,
    SYSTEM_BACKUP,
    
    // All permissions (super admin)
    ALL
}
