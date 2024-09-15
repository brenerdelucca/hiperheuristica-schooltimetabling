package com.hiperheuristica.schooltimetabling.choiceFunction;

import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Heuristic;
import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Performance;

import java.util.List;

public class ChoiceFunction {
    private List<Heuristic> heuristics;
    private double[] heuristicScores;

    public ChoiceFunction(List<Heuristic> heuristics) {
        this.heuristics = heuristics;
        this.heuristicScores = new double[heuristics.size()];
    }

    public Heuristic selectHeuristic() {
        //Atualizar pontuações
        updateScores();

        // Selecionar a heurística com a maior pontuação
        int bestIndex = 0;
        for(int i=1; i<heuristicScores.length; i++){
            if(heuristicScores[i] > heuristicScores[bestIndex]){
                bestIndex = i;
            }
        }

        Heuristic selectedHeuristic = heuristics.get(bestIndex);
        selectedHeuristic.incrementUsageCount(); //Incrementar a contagem de uso

        return selectedHeuristic;
    }

    private void updateScores() {
        //Atualizar as pontuações com base no desempenho e frequência de uso
        for(int i=0; i < heuristics.size(); i++) {
            Heuristic heuristic = heuristics.get(i);
            double performanceScore = calculatePureScore(heuristic.getPerformance());
            double usagePenalty = 1.0 / (1 + heuristic.getUsageCount()); //Penalidade pela frequência de uso
            heuristicScores[i] = performanceScore * usagePenalty; //Score ajustado
        }
    }

    public static double calculatePureScore(Performance performance) {
        //implementar calculo que gera um score baseado na performance

        return 0;
    }
}
