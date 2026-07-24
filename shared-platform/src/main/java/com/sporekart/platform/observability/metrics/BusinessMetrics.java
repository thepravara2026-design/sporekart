package com.sporekart.platform.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.DistributionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class BusinessMetrics {

    private static final Logger log = LoggerFactory.getLogger(BusinessMetrics.class);
    private final MeterRegistry registry;

    private final Counter ordersCreated;
    private final Counter ordersCompleted;
    private final Counter ordersCancelled;
    private final Counter paymentsProcessed;
    private final Counter paymentsFailed;
    private final Counter trainingRegistrations;
    private final Counter certificatesIssued;
    private final Counter productViews;
    private final Counter inventoryUpdates;
    private final Counter couponsApplied;
    private final Counter notificationsSent;
    private final Counter pluginUsage;
    private final Counter copilotQueries;
    private final AtomicLong activeUsers;
    private final AtomicLong revenue;
    private final Timer orderProcessingTime;
    private final Timer paymentProcessingTime;
    private final DistributionSummary orderValue;

    public BusinessMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.ordersCreated = Counter.builder("sporekart.business.orders.created")
            .description("Total orders created").register(registry);
        this.ordersCompleted = Counter.builder("sporekart.business.orders.completed")
            .description("Total orders completed").register(registry);
        this.ordersCancelled = Counter.builder("sporekart.business.orders.cancelled")
            .description("Total orders cancelled").register(registry);
        this.paymentsProcessed = Counter.builder("sporekart.business.payments.processed")
            .description("Total payments processed").register(registry);
        this.paymentsFailed = Counter.builder("sporekart.business.payments.failed")
            .description("Total payments failed").register(registry);
        this.trainingRegistrations = Counter.builder("sporekart.business.training.registrations")
            .description("Total training registrations").register(registry);
        this.certificatesIssued = Counter.builder("sporekart.business.certificates.issued")
            .description("Total certificates issued").register(registry);
        this.productViews = Counter.builder("sporekart.business.product.views")
            .description("Total product views").register(registry);
        this.inventoryUpdates = Counter.builder("sporekart.business.inventory.updates")
            .description("Total inventory updates").register(registry);
        this.couponsApplied = Counter.builder("sporekart.business.coupons.applied")
            .description("Total coupons applied").register(registry);
        this.notificationsSent = Counter.builder("sporekart.business.notifications.sent")
            .description("Total notifications sent").register(registry);
        this.pluginUsage = Counter.builder("sporekart.business.plugin.usage")
            .description("Total plugin invocations").register(registry);
        this.copilotQueries = Counter.builder("sporekart.business.copilot.queries")
            .description("Total copilot queries").register(registry);

        this.activeUsers = registry.gauge("sporekart.business.users.active", new AtomicLong(0));
        this.revenue = registry.gauge("sporekart.business.revenue.total", new AtomicLong(0));

        this.orderProcessingTime = Timer.builder("sporekart.business.order.processing.time")
            .description("Order processing time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.paymentProcessingTime = Timer.builder("sporekart.business.payment.processing.time")
            .description("Payment processing time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.orderValue = DistributionSummary.builder("sporekart.business.order.value")
            .description("Order value distribution")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);

        log.info("Business metrics initialized: 13 counters, 2 gauges, 2 timers, 1 summary");
    }

    public void recordOrderCreated() { ordersCreated.increment(); }
    public void recordOrderCompleted() { ordersCompleted.increment(); }
    public void recordOrderCancelled() { ordersCancelled.increment(); }
    public void recordPaymentProcessed() { paymentsProcessed.increment(); }
    public void recordPaymentFailed() { paymentsFailed.increment(); }
    public void recordTrainingRegistration() { trainingRegistrations.increment(); }
    public void recordCertificateIssued() { certificatesIssued.increment(); }
    public void recordProductView() { productViews.increment(); }
    public void recordInventoryUpdate() { inventoryUpdates.increment(); }
    public void recordCouponApplied() { couponsApplied.increment(); }
    public void recordNotificationSent() { notificationsSent.increment(); }
    public void recordPluginUsage() { pluginUsage.increment(); }
    public void recordCopilotQuery() { copilotQueries.increment(); }

    public void setActiveUsers(long count) { activeUsers.set(count); }
    public void addRevenue(long amount) { revenue.addAndGet(amount); }

    public void recordOrderProcessingTime(long millis) {
        orderProcessingTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordPaymentProcessingTime(long millis) {
        paymentProcessingTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordOrderValue(double value) {
        orderValue.record(value);
    }
}
