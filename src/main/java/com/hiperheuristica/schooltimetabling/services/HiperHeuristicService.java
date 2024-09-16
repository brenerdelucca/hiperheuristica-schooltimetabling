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

import java.util.ArrayList;
import java.util.List;

@Service
public class HiperHeuristicService {

    public void start(){
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

        for(int i = 0; i < 1000; i++) {
            selectedHeuristic = choiceFunction.selectHeuristic();
            solution = selectedHeuristic.apply(solution);

            //Atualizar a performance da heuristica
            performance = Performance.of(solution.get("score").asText());
            selectedHeuristic.updatePerformance(performance);
        }

        //armazenar todos os resultados gerados em algum local

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

}
