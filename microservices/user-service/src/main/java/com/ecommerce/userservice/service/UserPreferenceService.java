package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.UpdatePreferencesRequest;
import com.ecommerce.userservice.dto.response.UserPreferenceResponse;

/**
 * User preferences service
 */
public interface UserPreferenceService {
    
    // Preference CRUD
    UserPreferenceResponse getPreferences(Long userId);
    UserPreferenceResponse updatePreferences(Long userId, UpdatePreferencesRequest request);
    UserPreferenceResponse resetToDefaults(Long userId);
    
    // Notification preferences
    UserPreferenceResponse updateNotificationPreferences(Long userId, 
                                                        boolean email, 
                                                        boolean sms, 
                                                        boolean push);
    UserPreferenceResponse enableNotification(Long userId, String notificationType);
    UserPreferenceResponse disableNotification(Long userId, String notificationType);
    
    // Display preferences
    UserPreferenceResponse updateLanguage(Long userId, String language);
    UserPreferenceResponse updateCurrency(Long userId, String currency);
    UserPreferenceResponse updateTimezone(Long userId, String timezone);
    UserPreferenceResponse updateTheme(Long userId, String theme);
    
    // Privacy preferences
    UserPreferenceResponse updatePrivacySettings(Long userId, 
                                                 boolean profilePublic, 
                                                 boolean showOnlineStatus,
                                                 boolean allowMessagesFromStrangers);
    
    // Shopping preferences
    UserPreferenceResponse setDefaultShippingAddress(Long userId, Long addressId);
    UserPreferenceResponse setDefaultBillingAddress(Long userId, Long addressId);
    UserPreferenceResponse setDefaultPaymentMethod(Long userId, Long paymentMethodId);
    
    // Bulk operations
    void createDefaultPreferences(Long userId);
    void syncPreferences(Long userId, UserPreferenceResponse preferences);
}
