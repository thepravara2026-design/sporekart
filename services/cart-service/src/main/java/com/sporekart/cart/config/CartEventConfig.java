package com.sporekart.cart.config;

import com.sporekart.events.bus.EventBus;
import com.sporekart.events.bus.Subscription;
import com.sporekart.events.config.DomainEventRegistrar;
import com.sporekart.events.registry.EventRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartEventConfig {
    private static final Logger log = LoggerFactory.getLogger(CartEventConfig.class);

    private final EventBus eventBus;
    private Subscription cartSubscription;

    public CartEventConfig(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Bean
    public DomainEventRegistrar domainEventRegistrar(EventRegistry registry) {
        return new DomainEventRegistrar(registry);
    }

    @PostConstruct
    public void init() {
        cartSubscription = eventBus.subscribe("cart.*", "cart-service-handler",
                event -> log.debug("Cart event received: type={}, id={}",
                        event.getEventType(), event.getAggregateId()));
    }

    @PreDestroy
    public void cleanup() {
        if (cartSubscription != null) {
            eventBus.unsubscribe(cartSubscription);
        }
    }
}
