package com.hiperheuristica.schooltimetabling.choiceFunction;

import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Heuristic;
import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Performance;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ChoiceFunction {

    private final double pesoF1 = 0.9;
    private final double pesoF2 = 0.1;
    private final double pesoF3 = 1.5;

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
        //Atualizar as pontuações com base no desempenho
        for(int i=0; i < heuristics.size(); i++) {
            Heuristic heuristic = heuristics.get(i);
            double pureScore = calculatePureScore(heuristic.getPerformance());
            heuristicScores[i] = pureScore / pesoF1;
        }

        //Atualizar as pontuações com base no tempo decorrido desde que uma heurística foi selecionada pela última vez
        if(getWithUsageCountZero().isEmpty()){
            List<Long> minutesWithoutApplyPerHeuristic = getMinutesWithoutApplyPerHeuristic();
            for(int i = 0; i < heuristics.size(); i++){
                heuristicScores[i] += pesoF3 * ((double) minutesWithoutApplyPerHeuristic.get(i) / 10.0);
            }
        }
    }

    public static double calculatePureScore(Performance performance) {
        //calculo que gera um score baseado na performance
        double normalizador = 1000.0;

        return performance.getHard() + (performance.getSoft() / normalizador);
    }

    public List<Long> getMinutesWithoutApplyPerHeuristic(){
        return heuristics
                .stream()
                .map(heuristic -> Duration.between(heuristic.getLastApplication(), LocalDateTime.now()).toMinutes())
                .toList();
    }

    public List<Heuristic> getWithUsageCountZero() {
        return heuristics
                .stream()
                .filter(heuristic -> heuristic.getUsageCount() == 0)
                .toList();
    }
}
