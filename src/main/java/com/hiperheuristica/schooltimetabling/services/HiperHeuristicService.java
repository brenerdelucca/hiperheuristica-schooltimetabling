package com.hiperheuristica.schooltimetabling.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiperheuristica.schooltimetabling.choiceFunction.ChoiceFunction;
import com.hiperheuristica.schooltimetabling.choiceFunction.heuristics.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HiperHeuristicService {

    public void start(){
        //Recria o arquivo CSV de log ao iniciar a aplicação
        recriarArquivoCSV();

        List<Heuristic> heuristics = new ArrayList<>();
        heuristics.add(new GreatDeluge());
        heuristics.add(new HillClimbing());
        heuristics.add(new LateAcceptance());
        heuristics.add(new SimulatedAnnealing());
        heuristics.add(new StepCountingHillClimbing());
        heuristics.add(new StrategicOscillation());
        heuristics.add(new TabuSearch());
        heuristics.add(new VariableNeighborhoodDescent());

        ChoiceFunction choiceFunction = new ChoiceFunction(heuristics);

        JsonNode b3 = getB3();

        Heuristic selectedHeuristic = choiceFunction.selectHeuristic();
        JsonNode solution = selectedHeuristic.apply(b3);

        //Atualizar a performance da heuristica
        Performance performance = Performance.of(solution.get("score").asText());
        selectedHeuristic.updatePerformance(performance);

        //Atualiza scores após iteração
        choiceFunction.updateScores();

        //Adiciona usageCount da heuristica
        addScoreEUsageCountNoLog(choiceFunction, selectedHeuristic);

        for(int i = 0; i < 20; i++) {
            selectedHeuristic = choiceFunction.selectHeuristic();
            solution = selectedHeuristic.apply(solution);

            //Atualizar a performance da heuristica
            performance = Performance.of(solution.get("score").asText());
            selectedHeuristic.updatePerformance(performance);

            //Atualiza scores após iteração
            choiceFunction.updateScores();

            //Adiciona usageCount da heuristica
            addScoreEUsageCountNoLog(choiceFunction, selectedHeuristic);
        }

    }

    public JsonNode getB3(){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode b3 = null;
        try {
            b3 = objectMapper.readTree(restTemplate.exchange(
                    "http://localhost:8080/demo-data/B3",
                    HttpMethod.GET,
                    request,
                    String.class).getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return b3;
    }

    public static void addScoreEUsageCountNoLog(ChoiceFunction choiceFunction, Heuristic selectedHeuristic) {
        try (FileWriter writer = new FileWriter("src/main/resources/csvs/log.csv", true)) {
            for(int i=0; i<choiceFunction.getHeuristics().size(); i++) {
                String oneHeuristicName = choiceFunction.getHeuristics().get(i).getClass().getSimpleName().toLowerCase();
                String currentHeuristicName = selectedHeuristic.getClass().getSimpleName().toLowerCase();

                if(oneHeuristicName.equals(currentHeuristicName)) {
                    int currentHeuristicUsageCount = choiceFunction.getHeuristics().get(i).getUsageCount();
                    double currentHeuristicScore = choiceFunction.getHeuristicScores()[i];
                    writer.append(currentHeuristicUsageCount + ";" + currentHeuristicScore + "\n");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao adicionar score e usageCount: " + e.getMessage());
        }
    }

    public static void recriarArquivoCSV() {
        try (FileWriter writer = new FileWriter("src/main/resources/csvs/log.csv")) {
            // Adiciona cabeçalho no arquivo (se necessário)
            writer.append("JobId;Heuristic;BestPerformace;UsageCount;Score\n");
        } catch (IOException e) {
            System.err.println("Erro ao recriar o arquivo CSV de log: " + e.getMessage());
        }
    }

}
