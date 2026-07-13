package com.sporekart.ai.decision.infrastructure.security;
public class DecisionException extends RuntimeException {
    private final String code; private final int status;
    public DecisionException(String code, String message, int status) { super(message); this.code = code; this.status = status; }
    public String getCode() { return code; }
    public int getStatus() { return status; }
    public static DecisionException notFound(String message) { return new DecisionException("DEC_404", message, 404); }
    public static DecisionException badRequest(String message) { return new DecisionException("DEC_400", message, 400); }
    public static DecisionException evaluationFailed(String message) { return new DecisionException("DEC_500", message, 500); }
}
