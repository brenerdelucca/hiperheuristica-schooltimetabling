package com.hiperheuristica.schooltimetabling.choiceFunction;

import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Heuristic;
import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.Performance;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        //lista vazia so pra passar pro metodo e não dar erro
        List<HeuristicAndPerformance> listaVazia = new ArrayList<>();

        //Atualizar pontuações
        updateScores(listaVazia);

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

    public void updateScores(List<HeuristicAndPerformance> historic) {
        //Atualizar as pontuações com base no desempenho
        for(int i=0; i < heuristics.size(); i++) {
            Heuristic heuristic = heuristics.get(i);
            double pureScore = calculatePureScore(heuristic.getPerformance());
            heuristicScores[i] = pureScore / pesoF1;
        }

        //Atualizar a pontuação com base na dependencia entre heurísticas
        if(historic.size() > 1){
            updateScoreByRelation(historic);
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

    public void updateScoreByRelation(List<HeuristicAndPerformance> historic){
        //execução anterior
        HeuristicAndPerformance previousExecution = historic.get(historic.size() - 2);
        Performance previousPerformance = previousExecution.getPerformance();

        //execução de agora
        HeuristicAndPerformance currentExecution = historic.get(historic.size() - 1);
        Performance currentPerformance = currentExecution.getPerformance();

        //calcula quanto melhoro da anterior pra atual
        int hardDiff = Math.abs(previousPerformance.getHard()) - Math.abs(currentPerformance.getHard());
        int softDiff = Math.abs(previousPerformance.getSoft() - currentPerformance.getSoft());

        //pontuação baseada na melhoria da anterior pra atual
        double normalizador = 1000.0;

        double pointsBonusByRelation = hardDiff + (softDiff / normalizador);

        for(int i = 0; i < heuristics.size(); i++){
            if (heuristics.get(i).getClass().getSimpleName().equals(previousExecution.getHeuristicName())){
                heuristicScores[i] += pesoF2 * pointsBonusByRelation;
            }
        }
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
