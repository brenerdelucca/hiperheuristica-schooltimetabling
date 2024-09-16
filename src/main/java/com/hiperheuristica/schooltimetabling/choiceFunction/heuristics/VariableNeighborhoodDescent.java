package com.hiperheuristica.schooltimetabling.choiceFunction.heuristics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class VariableNeighborhoodDescent implements Heuristic {

    private List<Performance> performances;
    private Integer usageCount;

    @Override
    public JsonNode apply(JsonNode solution) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JsonNode> request = new HttpEntity<>(solution, headers);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode newSolution = null;
        try {
            newSolution = objectMapper.readTree(restTemplate.exchange("localhost:8080/timetables/variableNeighborhoodDescent",
                    HttpMethod.POST,
                    request,
                    String.class).getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return newSolution;
    }

    @Override
    public Performance getPerformance() {
        return performances.get(performances.size() - 1);
    }

    @Override
    public void updatePerformance(Performance performance) {
        this.performances.add(performance);
    }

    @Override
    public int getUsageCount() {
        return usageCount;
    }

    @Override
    public void incrementUsageCount() {
        usageCount++;
    }
}