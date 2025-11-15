package com.ecommerce.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User preferences entity - stores user-specific settings
 */
@Entity
@Table(name = "user_preferences", indexes = {
    @Index(name = "idx_pref_user_id", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Notification preferences
    @Column(name = "email_notifications")
    @Builder.Default
    private Boolean emailNotifications = true;

    @Column(name = "sms_notifications")
    @Builder.Default
    private Boolean smsNotifications = false;

    @Column(name = "push_notifications")
    @Builder.Default
    private Boolean pushNotifications = true;

    @Column(name = "marketing_emails")
    @Builder.Default
    private Boolean marketingEmails = true;

    @Column(name = "order_updates")
    @Builder.Default
    private Boolean orderUpdates = true;

    @Column(name = "price_alerts")
    @Builder.Default
    private Boolean priceAlerts = false;

    @Column(name = "newsletter")
    @Builder.Default
    private Boolean newsletter = false;

    // Display preferences
    @Column(length = 10)
    @Builder.Default
    private String language = "en";

    @Column(length = 10)
    @Builder.Default
    private String currency = "USD";

    @Column(length = 50)
    @Builder.Default
    private String timezone = "UTC";

    @Column(length = 20)
    @Builder.Default
    private String theme = "light"; // light, dark, auto

    // Privacy preferences
    @Column(name = "profile_public")
    @Builder.Default
    private Boolean profilePublic = false;

    @Column(name = "show_online_status")
    @Builder.Default
    private Boolean showOnlineStatus = true;

    @Column(name = "allow_messages_from_strangers")
    @Builder.Default
    private Boolean allowMessagesFromStrangers = false;

    // Shopping preferences
    @Column(name = "default_shipping_address_id")
    private Long defaultShippingAddressId;

    @Column(name = "default_billing_address_id")
    private Long defaultBillingAddressId;

    @Column(name = "default_payment_method_id")
    private Long defaultPaymentMethodId;

    @Column(name = "save_payment_methods")
    @Builder.Default
    private Boolean savePaymentMethods = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
