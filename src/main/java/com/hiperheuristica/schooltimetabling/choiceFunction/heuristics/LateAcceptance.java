package com.hiperheuristica.schooltimetabling.choiceFunction.heuristics;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class LateAcceptance implements Heuristic {

    private final String heuristicName = "lateAcceptance";
    private List<Performance> performances = new ArrayList<>(List.of(new Performance(0, 0)));
    private Integer usageCount = 0;

    @Override
    public JsonNode apply(JsonNode solution) {
        String newSolutionId = HeuristicHelper.startHeuristic(solution, heuristicName);

        HeuristicHelper.waitHeuristicExecution();

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
}