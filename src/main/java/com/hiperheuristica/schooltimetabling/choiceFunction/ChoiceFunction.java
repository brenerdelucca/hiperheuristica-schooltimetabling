package com.hiperheuristica.schooltimetabling.choiceFunction;

import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Heuristic;
import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Performance;
import lombok.Getter;

import java.util.List;

public class ChoiceFunction {
    @Getter
    private List<Heuristic> heuristics;
    @Getter
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

    public void updateScores() {
        //Atualizar as pontuações com base no desempenho e frequência de uso
        for(int i=0; i < heuristics.size(); i++) {
            Heuristic heuristic = heuristics.get(i);
            double pureScore = calculatePureScore(heuristic.getPerformance());
            double usagePenalty = 1.0 / (1 + heuristic.getUsageCount()); //Penalidade pela frequência de uso
            heuristicScores[i] = pureScore / usagePenalty; //Score ajustado
        }
    }

    public static double calculatePureScore(Performance performance) {
        //calculo que gera um score baseado na performance
        double normalizador = 1000.0;

        return performance.getHard() + (performance.getSoft() / normalizador);
    }
}
