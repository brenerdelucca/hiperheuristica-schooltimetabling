package com.hiperheuristica.schooltimetabling.choiceFunction.heuristics;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.LocalDateTime;

//@Data
public interface Heuristic {
    JsonNode apply(JsonNode solution);
    Performance getPerformance();
    void updatePerformance(Performance performance);
    int getUsageCount();
    void incrementUsageCount();
    void setLastApplication(LocalDateTime lastApplication);
    LocalDateTime getLastApplication();
}
