package com.sporekart.workflow.config;

import com.sporekart.workflow.domain.engine.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.application.service.*;
import com.sporekart.workflow.infrastructure.cache.WorkflowCacheService;
import com.sporekart.workflow.infrastructure.executor.MockActionExecutorService;
import com.sporekart.workflow.infrastructure.queue.MockWorkflowQueueService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowConfig {

    @Bean
    public WorkflowStateMachine workflowStateMachine() {
        return new WorkflowStateMachine();
    }

    @Bean
    public WorkflowDefinitionEngine workflowDefinitionEngine(WorkflowRepositoryPort repository) {
        return new WorkflowDefinitionEngine(repository);
    }

    @Bean
    public WorkflowDecisionEngine workflowDecisionEngine() {
        return new WorkflowDecisionEngine();
    }

    @Bean
    public WorkflowExecutionEngine workflowExecutionEngine(WorkflowRepositoryPort repository, WorkflowStateMachine stateMachine) {
        return new WorkflowExecutionEngine(repository, stateMachine);
    }

    @Bean
    public WorkflowSimulationEngine workflowSimulationEngine(WorkflowRepositoryPort repository, WorkflowDecisionEngine decisionEngine) {
        return new WorkflowSimulationEngine(repository, decisionEngine);
    }

    @Bean
    public WorkflowAuditEngine workflowAuditEngine(WorkflowRepositoryPort repository) {
        return new WorkflowAuditEngine(repository);
    }

    @Bean
    public WorkflowRegistryService workflowRegistryService(WorkflowRepositoryPort repository, WorkflowDefinitionEngine definitionEngine) {
        return new WorkflowRegistryService(repository, definitionEngine);
    }

    @Bean
    public WorkflowTelemetryService workflowTelemetryService(WorkflowRepositoryPort repository) {
        return new WorkflowTelemetryService(repository);
    }

    @Bean
    public WorkflowCacheService workflowCacheService(WorkflowConfigBinding config) {
        return new WorkflowCacheService(config.getCache().getTtlSeconds(), config.getCache().getMaxSize());
    }

    @Bean
    public WorkflowOrchestratorService workflowOrchestratorService(
        WorkflowRepositoryPort repository,
        WorkflowExecutionEngine executionEngine,
        WorkflowSimulationEngine simulationEngine,
        WorkflowAuditEngine auditEngine,
        WorkflowRegistryService registryService,
        WorkflowTelemetryService telemetry,
        MockWorkflowQueueService queueService,
        MockActionExecutorService actionExecutor,
        WorkflowCacheService cacheService,
        WorkflowStateMachine stateMachine
    ) {
        return new WorkflowOrchestratorService(repository, executionEngine, simulationEngine,
            auditEngine, registryService, telemetry, queueService, actionExecutor, cacheService, stateMachine);
    }

    @ConfigurationProperties(prefix = "workflow")
    @Bean
    public WorkflowConfigBinding workflowConfigBinding() {
        return new WorkflowConfigBinding();
    }

    public static class WorkflowConfigBinding {
        private EngineConfig engine = new EngineConfig();
        private RuntimeConfig runtime = new RuntimeConfig();
        private QueueConfig queue = new QueueConfig();
        private RetryConfig retry = new RetryConfig();
        private CacheConfig cache = new CacheConfig();
        private TelemetryConfig telemetry = new TelemetryConfig();
        private ApprovalConfig approval = new ApprovalConfig();
        private AuditConfig audit = new AuditConfig();
        private boolean debug;

        public EngineConfig getEngine() { return engine; }
        public void setEngine(EngineConfig engine) { this.engine = engine; }
        public RuntimeConfig getRuntime() { return runtime; }
        public void setRuntime(RuntimeConfig runtime) { this.runtime = runtime; }
        public QueueConfig getQueue() { return queue; }
        public void setQueue(QueueConfig queue) { this.queue = queue; }
        public RetryConfig getRetry() { return retry; }
        public void setRetry(RetryConfig retry) { this.retry = retry; }
        public CacheConfig getCache() { return cache; }
        public void setCache(CacheConfig cache) { this.cache = cache; }
        public TelemetryConfig getTelemetry() { return telemetry; }
        public void setTelemetry(TelemetryConfig telemetry) { this.telemetry = telemetry; }
        public ApprovalConfig getApproval() { return approval; }
        public void setApproval(ApprovalConfig approval) { this.approval = approval; }
        public AuditConfig getAudit() { return audit; }
        public void setAudit(AuditConfig audit) { this.audit = audit; }
        public boolean isDebug() { return debug; }
        public void setDebug(boolean debug) { this.debug = debug; }

        public static class EngineConfig {
            private boolean enabled = true;
            private String defaultType = "ORDER";
            private boolean simulation = true;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public String getDefaultType() { return defaultType; }
            public void setDefaultType(String defaultType) { this.defaultType = defaultType; }
            public boolean isSimulation() { return simulation; }
            public void setSimulation(boolean simulation) { this.simulation = simulation; }
        }

        public static class RuntimeConfig {
            private boolean enabled = true;
            private int maxConcurrent = 100;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public int getMaxConcurrent() { return maxConcurrent; }
            public void setMaxConcurrent(int maxConcurrent) { this.maxConcurrent = maxConcurrent; }
        }

        public static class QueueConfig {
            private boolean enabled = true;
            private int maxSize = 1000;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public int getMaxSize() { return maxSize; }
            public void setMaxSize(int maxSize) { this.maxSize = maxSize; }
        }

        public static class RetryConfig {
            private boolean enabled = true;
            private int maxAttempts = 3;
            private int backoffMs = 1000;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public int getMaxAttempts() { return maxAttempts; }
            public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
            public int getBackoffMs() { return backoffMs; }
            public void setBackoffMs(int backoffMs) { this.backoffMs = backoffMs; }
        }

        public static class CacheConfig {
            private boolean enabled = true;
            private int ttlSeconds = 300;
            private int maxSize = 500;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public int getTtlSeconds() { return ttlSeconds; }
            public void setTtlSeconds(int ttlSeconds) { this.ttlSeconds = ttlSeconds; }
            public int getMaxSize() { return maxSize; }
            public void setMaxSize(int maxSize) { this.maxSize = maxSize; }
        }

        public static class TelemetryConfig {
            private boolean enabled = true;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
        }

        public static class ApprovalConfig {
            private boolean enabled = true;
            private boolean requireAll = true;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public boolean isRequireAll() { return requireAll; }
            public void setRequireAll(boolean requireAll) { this.requireAll = requireAll; }
        }

        public static class AuditConfig {
            private boolean enabled = true;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
        }
    }
}
