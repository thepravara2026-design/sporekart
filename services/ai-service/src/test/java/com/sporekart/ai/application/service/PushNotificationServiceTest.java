package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.MobileDevice;
import com.sporekart.ai.domain.model.PushNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PushNotificationServiceTest {
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private MobileService mobileService;

    @InjectMocks
    private PushNotificationService pushNotificationService;

    private UUID userId;
    private UUID deviceId;
    private MobileDevice device;
    private PushNotification notification;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        deviceId = UUID.randomUUID();
        device = new MobileDevice(deviceId, userId, "Device", "iOS", "18.0", "1.0.0", "token123");
        notification = new PushNotification(UUID.randomUUID(), deviceId, userId,
                "Order Shipped", "Your order is on the way", "ORDER");
        notification.setActionUrl("/orders/123");

        org.mockito.Mockito.lenient().when(mobileService.getDevice(deviceId)).thenReturn(device);
        org.mockito.Mockito.lenient().when(mobileService.sendNotification(deviceId, userId,
                "Order Shipped", "Your order is on the way", "ORDER", "/orders/123"))
                .thenReturn(notification);
    }

    @Test
    void shouldSendNotification() {
        var result = pushNotificationService.sendNotification(deviceId, userId,
                "Order Shipped", "Your order is on the way", "ORDER", "/orders/123");

        assertNotNull(result);
        assertEquals("Order Shipped", result.getTitle());
        assertEquals("Your order is on the way", result.getBody());
        assertEquals(deviceId, result.getDeviceId());
        verify(kafkaTemplate, atLeastOnce()).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldHandleNotificationOpen() {
        UUID notificationId = UUID.randomUUID();

        assertDoesNotThrow(() -> pushNotificationService.handleNotificationOpen(notificationId, deviceId));
        verify(kafkaTemplate).send(eq("push-notification-opened"), eq("opened-" + notificationId), anyString());
    }

    @Test
    void shouldGetDeliveryMetrics() {
        Map<String, Object> metrics = pushNotificationService.getDeliveryMetrics();

        assertNotNull(metrics);
        assertTrue(metrics.containsKey("deliveryRate"));
        assertTrue(metrics.containsKey("openRate"));
        assertEquals(98.0, metrics.get("deliveryRate"));
        assertEquals(65.0, metrics.get("openRate"));
    }

    @Test
    void shouldSendTopicNotification() {
        assertDoesNotThrow(() -> pushNotificationService.sendTopicNotification("orders",
                "New Promotion", "50% off on seeds", "SYSTEM"));

        verify(kafkaTemplate).send(eq("push-notification-topic"), eq("topic-orders"), anyString());
    }
}
