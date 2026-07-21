package com.sporekart.ai.providers.contracts;

public record AudioContract(
    String model,
    byte[] audioData,
    String format,
    String language,
    AudioOperation operation
) {
    public enum AudioOperation { TRANSCRIPTION, TRANSLATION, SPEECH }
}
