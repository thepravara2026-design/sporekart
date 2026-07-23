package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.BusinessForecast;
import com.sporekart.bi.copilot.dto.ForecastRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ForecastingEngine {

    private static final Logger log = LoggerFactory.getLogger(ForecastingEngine.class);

    public BusinessForecast forecast(ForecastRequest request) {
        log.debug("Generating forecast for metric: {}", request.metric());
        return null;
    }
}
