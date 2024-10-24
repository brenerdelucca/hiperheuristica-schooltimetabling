package com.hiperheuristica.schooltimetabling.choiceFunction;

import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Heuristic;
import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Performance;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeuristicAndPerformance {
    private String heuristicName;
    private Performance performance;
}
