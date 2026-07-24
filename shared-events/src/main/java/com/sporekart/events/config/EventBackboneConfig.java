package com.sporekart.events.config;

import com.sporekart.events.bus.EventBus;
import com.sporekart.events.bus.InMemoryEventBus;
import com.sporekart.events.bus.EventPublisher;
import com.sporekart.events.dlq.DeadLetterQueue;
import com.sporekart.events.dlq.InMemoryDeadLetterQueue;
import com.sporekart.events.registry.DefaultEventRegistry;
import com.sporekart.events.registry.EventRegistry;
import com.sporekart.events.retry.DefaultRetryEngine;
import com.sporekart.events.retry.RetryEngine;
import com.sporekart.events.retry.RetryPolicy;
import com.sporekart.events.routing.DefaultEventRouter;
import com.sporekart.events.routing.EventRouter;
import com.sporekart.events.security.EventSecurity;
import com.sporekart.events.serializer.EventSerializer;
import com.sporekart.events.store.EventStore;
import com.sporekart.events.store.InMemoryEventStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBackboneConfig {

    @Bean
    @ConditionalOnMissingBean
    public EventRegistry eventRegistry() {
        return new DefaultEventRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventRouter eventRouter() {
        return new DefaultEventRouter();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryPolicy retryPolicy() {
        return RetryPolicy.defaultPolicy();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryEngine retryEngine(RetryPolicy retryPolicy) {
        return new DefaultRetryEngine(retryPolicy);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeadLetterQueue deadLetterQueue() {
        return new InMemoryDeadLetterQueue();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventStore eventStore() {
        return new InMemoryEventStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventSecurity eventSecurity() {
        return new EventSecurity();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventSerializer eventSerializer() {
        return new EventSerializer();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBus eventBus(EventRegistry registry, EventRouter router,
                             RetryEngine retryEngine, DeadLetterQueue dlq,
                             EventStore eventStore, EventSecurity security) {
        return new InMemoryEventBus(registry, router, retryEngine, dlq, eventStore, security);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventPublisher eventPublisher(EventBus eventBus) {
        return eventBus;
    }
}
