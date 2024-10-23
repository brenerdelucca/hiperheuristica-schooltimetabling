package com.hiperheuristica.schooltimetabling.choiceFunction.heuristics;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GreatDeluge implements Heuristic {

    private final String heuristicName = "greatDeluge";
    private List<Performance> performances = new ArrayList<>(List.of(new Performance(0, 0)));
    private Integer usageCount = 0;
    private LocalDateTime lastApplication;

    @Override
    public JsonNode apply(JsonNode solution) {
        String newSolutionId = HeuristicHelper.startHeuristic(solution, heuristicName);

        HeuristicHelper.verifySolverStatus(newSolutionId);

        return HeuristicHelper.getSolution(newSolutionId, heuristicName);
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

    @Override
    public void setLastApplication(LocalDateTime lastApplication) {
        this.lastApplication = lastApplication;
    }

    @Override
    public LocalDateTime getLastApplication() {
        return lastApplication;
    }
}
