package com.sporekart.ai.eventcatalog.api;

import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventDependencyDto;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventFlowDto;
import java.util.List;
import java.util.Map;

public interface EventVisualizationService {

    EventFlowDto getEventFlow();

    List<EventDependencyDto> getEventDependencies();

    Map<String, List<String>> getModuleEventMap();
}
