package com.hiperheuristica.schooltimetabling.choiceFunction.heuristics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TabuSearch implements Heuristic {

    private List<Performance> performances = new ArrayList<>(List.of(new Performance(0, 0)));
    private Integer usageCount = 0;

    @Override
    public JsonNode apply(JsonNode solution) {
        System.out.println("Iniciando execução do Tabu Search.");

        String newSolutionId = HeuristicHelper.startHeuristic(solution, "tabuSearch");

        HeuristicHelper.waitHeuristicExecution();

        System.out.println("Execução do Tabu Search finalizada.");

        return HeuristicHelper.getSolution(newSolutionId);
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
